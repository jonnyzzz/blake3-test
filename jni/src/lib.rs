// This is the interface to the JVM that we'll
// call the majority of our methods on.
use jni::JNIEnv;

// These objects are what you should use as arguments to your native function.
// They carry extra lifetime information to prevent them escaping this context
// and getting used after being GC'd.
use jni::objects::{JClass, ReleaseMode};

// This is just a pointer. We'll be returning it from our function.
// We can't return one of the objects with lifetime information because the
// lifetime checker won't let us.
use jni::sys::{jbyteArray, jbyte};

use std::{mem};

#[no_mangle]
pub extern "system" fn Java_HelloWorld_helloByte(
    env: JNIEnv,
    _class: JClass,
    input: jbyteArray,
) -> jbyteArray {
    let length = env.get_array_length(input).unwrap() as usize;
    let j_array_bytes = env.get_byte_array_elements(input).unwrap().0;
    let j_view = j_array_bytes as *const u8;
    let u_size: usize = mem::size_of::<jbyte>() * length / mem::size_of::<u8>();
    let array: &[u8] = unsafe { std::slice::from_raw_parts(j_view, u_size) };

    let mut hasher = blake3::Hasher::new();
    hasher.update(array);
    let hash = hasher.finalize();

    env.release_byte_array_elements(
        input,
        unsafe { j_array_bytes.as_mut().unwrap() },
        ReleaseMode::NoCopyBack
    ).unwrap();

    // Then we have to create a new java byte[] to return.
    let output = env.byte_array_from_slice(hash.as_bytes()).unwrap();
    // Finally, extract the raw pointer to return.
    output
}

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {}
}