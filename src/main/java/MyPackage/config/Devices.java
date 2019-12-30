package MyPackage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class Devices {

    public enum Androids {
        Samsung_phone, Sony_phone, Asus_phone, Xiomi_phone,
        Samsung_tablet, Sony_tablet, Asus_tablet, Xiomi_tablet;

        @Override
        public String toString() {
            return super.toString().replace('_', ' ');
        }
    }

    @Bean
    public List<String> getAndroids() {
        return Stream.of(Androids.values()).map(Androids::toString).collect(Collectors.toList());
    }

    public enum Ios {
        Iphone, Ipad;
    }

    @Bean
    public List<String> getIos() {
        return Stream.of(Ios.values()).map(Ios::toString).collect(Collectors.toList());
    }

    public enum Unknonws {
        Another_phone, Another_tablet, Unknown;

        @Override
        public String toString() {
            return super.toString().replace('_', ' ');
        }
    }

    @Bean
    public List<String> getUnknown() {
        return Stream.of(Unknonws.values()).map(Unknonws::toString).collect(Collectors.toList());
    }

}
