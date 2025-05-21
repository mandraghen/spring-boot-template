package com.template.sbtemplate.config.redis;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.redisson.api.NameMapper;

public class PrefixNameMapper implements NameMapper {

    private final String prefix;

    public PrefixNameMapper(@JsonProperty("prefix") String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String map(String name) {
        return prefix + name;
    }

    @Override
    public String unmap(String name) {
        return name.substring(prefix.length());
    }
}
