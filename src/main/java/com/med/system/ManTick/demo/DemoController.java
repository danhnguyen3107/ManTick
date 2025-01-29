
package com.med.system.ManTick.demo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

  @GetMapping
  public ResponseEntity<String> sayHello() {
    // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // // System.out.println(authentication);
    //  System.out.println("Print Authority: " + authentication.getName());
    // if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
    //   return ResponseEntity.ok("You are not authenticated");
    // }
    return ResponseEntity.ok("Hello from secured endpoint");
  }

}