package homework5.mapper;

/*
@Configuration
public class UserDtoMapperConfig {

    @Bean
    public ModelMapper userDtoMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(SysUser.class, UserDto.class)
                .addMapping(SysUser::getUserName, UserDto::setUserName)
                .addMappings(m -> m.map(src -> src.getSysRoles().stream()
                        .map(SysRole::getRoleName)
                        .collect(Collectors.joining(", ")), UserDto::setSysRoles));

        return mapper;
    }
}

 */

