package com.mantono.argumentloader

/**
 * Read an environment variable from [System.getEnv()], and give an optional value as fallback.
 *
 * @param key Key for environment variable to check for value
 * @param default Optional fallback value to use in case no value exists for key
 * @return Value for key or fallback value
 * @throws RuntimeException if no value exists for key and no fallback value is given
 */
fun envVar(key: String, default: String? = null): String
{
	val error = "Environment variable '$key' could not be found, and no default value was given"
	return System.getenv(key) ?: default ?: throw RuntimeException(error)
}