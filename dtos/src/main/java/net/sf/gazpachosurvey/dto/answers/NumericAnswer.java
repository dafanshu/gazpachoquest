package net.sf.gazpachosurvey.dto.answers;

public class NumericAnswer extends AbstractAnswer {

    private Integer value;

    public NumericAnswer() {
        super();
    }

    public NumericAnswer(Integer value) {
        super();
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static NumericAnswer fromValue(Integer value) {
        return new NumericAnswer(value);
    }

}
