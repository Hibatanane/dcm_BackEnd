package prjt.dcm.Shared;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
public class ErrorMessage {
    String message;
    Integer code;
}
