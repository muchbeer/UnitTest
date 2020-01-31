package raum.muchbeer.unittest.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataStateStatus<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    public DataStateStatus(@NonNull Status status, @Nullable T data, @NonNull String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> DataStateStatus<T> success(@NonNull T data, @NonNull String message) {
        return new DataStateStatus<>(Status.SUCCESS, data, message);
    }

    public static <T> DataStateStatus<T> error( @Nullable T data, @NonNull String msg) {
        return new DataStateStatus<>(Status.ERROR, data, msg);
    }

    public static <T> DataStateStatus<T> loading(@Nullable T data) {
        return new DataStateStatus<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING}

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass() || obj.getClass() != DataStateStatus.class){
            return false;
        }

        DataStateStatus<T> resource = (DataStateStatus) obj;

        if(resource.status != this.status){
            return false;
        }

        if(this.data != null){
            if(resource.data != this.data){
                return false;
            }
        }

        if(resource.message != null){
            if(this.message == null){
                return false;
            }
            if(!resource.message.equals(this.message)){
                return false;
            }
        }

        return true;
    }
}
