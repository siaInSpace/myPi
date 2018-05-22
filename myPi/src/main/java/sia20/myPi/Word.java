package sia20.myPi;

public class Word {
    private long value;
    private String ref;

    public Word(String ref, long value) {
        this.value = value;
        this.ref = ref;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public static long checkRef(Word[] words, String  ref){
        long res = 0;
        for (Word word: words) {
            if (word.getRef().equals(ref)){
                res = word.getValue();
            }
        }
        return res;
    }
}
