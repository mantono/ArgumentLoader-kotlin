package com.mantono.argumentloader

/**
 * Interface for program options that can be loaded through the command line
 * argument vector. The purpose of this interface, and its implementing classes,
 * is to simplify argument loading and validation with settings that are sent to
 * the main method at application launch.

 * @author Anton &Ouml;sterberg
 */
interface ProgramOption
{
	/**
	 * @return returns the short flag representation for this option that is
	 * * combined with a single dash ("-").
	 */
	val shortFlag: Char

	/**

	 * @return returns the long flag representation for this option that is
	 * * combined with a double dash ("--").
	 */
	val longFlag: String

	/**

	 * @return a description that describes what this option does.
	 */
	val description: String

	/**

	 * @return the default value.
	 */
	val defaultValue: String

	/**

	 * @return true if this option takes an argument, else false.
	 */
	val takesArgument: Boolean

	/**
	 * Checks whether a [String] matches any of the options for this
	 * class.

	 * @param input the [String] that should be checked whether it matched
	 * * or not.
	 * *
	 * @return true if the input is equal to the short flag or the long flag
	 * * without the dash prefix.
	 */
	fun matches(input: String): Boolean = input == "-" + shortFlag || input == "--" + longFlag

	/**
	 * @return a text that is printed whenever a user calls the help flag, if
	 * * such a flag exists for the implementing class or enumerate.
	 */
	fun helpDescription(): String = "-$shortFlag, --$longFlag\n\t$description\n"
}
