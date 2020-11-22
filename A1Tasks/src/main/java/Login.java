import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Login {
    private String application;
    private String appAccountName;
    private boolean isActive;
    private String JobTitle;
    private String Department;
}
