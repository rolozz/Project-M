package com.java.project.userinfoservice.feign;

import com.java.project.userinfoservice.dto.RequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AuthServer", url = "http://localhost:8082")
public interface AuthBackUpSave {

    @PostMapping("/api/register")
    ResponseEntity<String> register(@RequestBody RequestDto requestDto);

}
