package my_mall.utils;

public class TLUtils {
    private static final ThreadLocal<Long> USER_HOLDER =new ThreadLocal();

    public static Long getUserId() {
        return USER_HOLDER.get();
    }
    public static void setUserId(Long userId) {
        USER_HOLDER.set(userId);
    }
    public static void remove() {
        USER_HOLDER.remove();
    }
}
