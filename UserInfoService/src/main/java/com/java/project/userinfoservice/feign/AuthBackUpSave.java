package com.java.project.userinfoservice.feign;

import com.java.project.userinfoservice.dto.RequestDto;
import com.java.project.userinfoservice.dto.UpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AuthServer", url = "http://localhost:8082/api/auth")
public interface AuthBackUpSave {

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody RequestDto requestDto);

    @PostMapping("/update")
    ResponseEntity<String> update(@RequestBody UpdateDto updateDto);

}
