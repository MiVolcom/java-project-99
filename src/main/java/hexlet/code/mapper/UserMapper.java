package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(encoder.encode(password));
    }

    @BeforeMapping
    public void encryptPasswordUpdate(UserUpdateDTO userUpdateDTO, @MappingTarget User user) {
        var password = userUpdateDTO.getPassword();
        if (password != null && password.isPresent()) {
            user.setPasswordDigest(encoder.encode(password.get()));
        }
    }
    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO model);

    public abstract UserDTO map(User model);

    @Mapping(source = "password", target = "passwordDigest")
    public abstract void update(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

}
