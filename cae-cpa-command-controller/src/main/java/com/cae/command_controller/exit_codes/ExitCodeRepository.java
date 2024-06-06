package com.cae.command_controller.exit_codes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ExitCodeRepository {

     SUCCESS(0),
     INTERNAL_ERROR(1),
     INPUT_ERROR(2),
     COMMAND_NOT_FOUND(127),
     RESOURCE_NOT_FOUND(30);

     private final Integer code;

     public static String getAllCodes(){
         return Arrays.stream(ExitCodeRepository.values())
                 .map(value -> value.name() + ": " + value.code.toString())
                 .reduce("", (previous, next) -> previous.concat(next).concat("\n"));
     }

}
