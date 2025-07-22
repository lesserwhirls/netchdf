package com.sunya.netchdf.cli

// import com.sunya.netchdf.openNetchdfFile
// import kotlinx.cli.*

/*
object ncdump {
    @JvmStatic
    fun main(args: Array<String>) {
        val parser = ArgParser("ncdump")
        val filename by parser.option(
            ArgType.String,
            shortName = "in",
            description = "Directory containing input election record"
        ).required()

        parser.parse(args)

        openNetchdfFile(filename, false).use { myfile ->
            if (myfile == null) {
                println("*** not a netchdf file = $filename")
                return
            }
            println("${myfile.type()} $filename ")
            println(myfile.cdl())
        }
    }
}

 */