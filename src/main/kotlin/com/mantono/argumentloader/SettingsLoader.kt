package com.mantono.argumentloader

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.*


/**
 * SettingsLoader is meant to simplify the use of loading configurations and
 * settings flags through the standard arguments vector (String[] args) in a
 * program entry point class and configurations files. Any class
 * implementing the [ProgramOption] interface will also have to define a
 * default option for every setting, in case neither command line flags or a
 * configuration file is used.

 * @author Anton &Ouml;sterberg
 * *
 * *
 * @param <T> the [Enum] class that is used for defining the settings
 * * flags.
</T> */
class SettingsLoader<T : Enum<T>>(private val enumClass: Class<T>) where T : ProgramOption
{
	private val settings: EnumMap<T, String>

	init
	{
		this.settings = loadDefaultSettings()
	}

	/**
	 * Reads the default settings of [Enum] class `T`.

	 * @return the default settings as an [EnumMap].
	 */
	private fun loadDefaultSettings(): EnumMap<T, String>
	{
		val defaultSettings = EnumMap<T, String>(enumClass)
		for(op in values())
			defaultSettings.put(op, op.defaultValue)
		return defaultSettings
	}

	/**
	 * Takes the [String] representation of a implementing
	 * [ProgramOption] and retrieves its corresponding enumerate key.

	 * @param string the [String] for which an option is sought.
	 * *
	 * @return the option of type `T`.
	 */
	private fun getOption(string: String): T? = values().firstOrNull { it.matches(string) }

	/**
	 * @return all the [Enum] keys for this class instantiated enum class.
	 */
	private fun values(): Array<T> = enumClass.enumConstants

	/**
	 * Apply the command line flags that are sent through the argument vector,
	 * overwriting any default options or options from any configuration file.
	 * Options set through the argument vector has the highest priority.

	 * @param args the arguments that will be applied to the current
	 * * configuration.
	 */
	fun applyAgrumentVector(args: Array<String>)
	{
		var i = 0
		while (i < args.size)
		{
			val option = getOption(args[i])
			nullCheck(option, args[i])
			if (option == getOption("--help"))
			{
				for (op in values())
					System.out.println(op)
				System.exit(0)
			}
			try
			{
				settings.put(option!!, args[i + 1])
			}
			catch (exception: ArrayIndexOutOfBoundsException)
			{
				System.err.println("Flag " + args[i] + " requires an argument.")
				System.exit(3)
			}

			i += 2
		}
	}

	/**
	 * Check whether a found option actually exists or is null. If it is null,
	 * an error message is written to [System.err] and the program exits
	 * with exit code <tt>1</tt>.

	 * @param option that will be checked whether it is <tt>null</tt> or not.
	 * *
	 * @param flag the input [String] that was given as an argument.
	 */
	private fun nullCheck(option: T?, flag: String)
	{
		if (option == null)
		{
			System.err.println("Argument $flag is not a valid flag. See --help for options.")
			System.exit(1)
		}
	}

	/**
	 * Reads and parses the content of a config file.

	 * @param fileName name of the file that will be read.
	 * *
	 * @return true if a file was found, read and successfully parsed, else
	 * * false.
	 * *
	 * @throws IOException if there was a problem reading from the file.
	 */
	fun readConfig(fileName: String): Boolean
	{
		val file = File(fileName)
		if (!file.exists())
			return false

		val list = Files.readAllLines(file.toPath(), Charset.defaultCharset())
		for (line in list)
			parseLine(line)

		return true
	}

	/**
	 * Parses the content of a line from a config file, making sure it
	 * correspondents to valid option as defined by the implementing
	 * [ProgramOption] class.

	 * Options are read a key value pairs, which are split by the used of
	 * '&#61;'.

	 * @param line the line that will be parsed.
	 */
	private fun parseLine(line: String)
	{
		val keyValue = line.split("=".toRegex(), 2).toTypedArray()
		val key = "--" + keyValue[0].toLowerCase().trim({it <= ' '})
		val value = keyValue[1].trim({it <= ' '})
		val option = getOption(key)
		nullCheck(option, key)
		settings.put(option!!, value)
	}

	/**
	 * Returns an unmodifiable copy of the content of the current settings, as
	 * the settings are only meant to be modified by command line flags or the
	 * config file.

	 * @return the loaded settings.
	 */
	fun settings(): Map<T, String> = settings
}