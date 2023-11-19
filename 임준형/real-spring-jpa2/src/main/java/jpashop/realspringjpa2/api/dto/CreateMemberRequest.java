package jpashop.realspringjpa2.api.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateMemberRequest {
    @NotEmpty
    private String name;
}
