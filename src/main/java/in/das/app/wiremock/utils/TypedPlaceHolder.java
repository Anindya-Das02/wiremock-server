package in.das.app.wiremock.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TypedPlaceHolder {
    private final String placeHolder;
    private final String type;
    private final String completePlaceHolder;

    public TypedPlaceHolder(String placeHolder, String type, String completePlaceHolder) {
        this.placeHolder = placeHolder;
        this.type = type;
        this.completePlaceHolder = completePlaceHolder;
    }
}
