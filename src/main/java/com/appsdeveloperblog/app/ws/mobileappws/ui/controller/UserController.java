package com.appsdeveloperblog.app.ws.mobileappws.ui.controller;


import com.appsdeveloperblog.app.ws.mobileappws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.mobileappws.service.UserService;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailsRequestModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id){
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);
        return returnValue;
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
//    })
    @GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

//        Type listType = new TypeToken<List<UserRest>>() {
//        }.getType();
//        returnValue = new ModelMapper().map(users, listType);

		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}

        return returnValue;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception
    {

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
//    })
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
//        userDto = new ModelMapper().map(userDetails, UserDto.class);
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(id, userDto);
//        returnValue = new ModelMapper().map(updateUser, UserRest.class);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
//    })
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }


}
