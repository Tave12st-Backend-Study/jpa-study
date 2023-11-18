package jpashop.realspringjpa2.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateMemberResponse {

    private Long id;
    private String name;

}
