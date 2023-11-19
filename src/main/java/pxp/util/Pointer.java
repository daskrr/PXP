package pxp.util;

/**
 * Used for storing an object, but not losing its reference if it's set to null.<br/>
 * It's simply like a pointer, but can't store primitives. (use their counterpart)
 * @param <T> the type of the object to store.
 */
public class Pointer<T> {
   public T value;

   public Pointer(T value) {
      this.value = value;
   }

   public String toString() {
      return "[*] " + (this.value == null ? null : this.value.toString());
   }
}
