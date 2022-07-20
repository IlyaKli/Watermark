package watermark

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.exitProcess

var DiffX = 0

var DiffY = 0

var xPosition = 0

var yPosition = 0

var widthImagePos = 0

var heightImagePos = 0

var widthWatermarkPos = 0

var heightWatermarkPos = 0

var transparency = 0

var transparencyWatermark = 4

var jpg = ".jpg"

var png = ".png"

var extension = ""

fun fileName() {
    println("Input the image filename:")
}

fun fileNameWatermark() {
    println("Input the watermark image filename:")
}

fun sizeImage(fileName: String) {
    var imageFile = File(fileName)
    var image = ImageIO.read(imageFile)
    widthImagePos = image.width
    heightImagePos = image.height
}

fun fileNameExists(fileName: String) {
    if (File(fileName).exists() == false) {
        println("The file $fileName doesn't exist.")
        exitProcess(0)
    }

    var imageFileEx = File(fileName)
    var imageEx = ImageIO.read(imageFileEx)
    var numColComEx = imageEx.colorModel.numColorComponents

    if (numColComEx !== 3) {
        println("The number of image color components isn't 3.")
        exitProcess(0)
    }

    var pixEx = imageEx.colorModel.pixelSize

    if (pixEx !== 24 && pixEx !== 32) {
        println("The image isn't 24 or 32-bit.")
        exitProcess(0)
    }
}

fun fileNameWatermarkExists(fileName: String) {
    if (File(fileName).exists() == false) {
        println("The file $fileName doesn't exist.")
        exitProcess(0)
    }

    var imageFileEx = File(fileName)
    var imageEx = ImageIO.read(imageFileEx)
    var numColComEx = imageEx.colorModel.numColorComponents

    if (numColComEx !== 3) {
        println("The number of watermark color components isn't 3.")
        exitProcess(0)
    }

    var pixEx = imageEx.colorModel.pixelSize

    if (pixEx !== 24 && pixEx !== 32) {
        println("The watermark isn't 24 or 32-bit.")
        exitProcess(0)
    }

    widthWatermarkPos = imageEx.width
    heightWatermarkPos = imageEx.height


    if (widthImagePos !== 0 && heightImagePos !== 0) {

        if (widthImagePos < widthWatermarkPos || heightImagePos < heightWatermarkPos) {
            println("The watermark's dimensions are larger.")
            exitProcess(0)
        }
    }
}

fun transparency() {
    println("Input the watermark transparency percentage (Integer 0-100):")
}

fun checkTransparency(transparencyIn: Double) {
    if (transparencyIn <= 0 || transparencyIn >= 100) {
        println("The transparency percentage is out of range.")
        exitProcess(0)
    }
    else if (transparencyIn * 10 % 10 !== 0.0) {
        println("The transparency percentage isn't an integer number.")
        exitProcess(0)
    }
    else transparency = transparencyIn.toInt()
}

fun transparencyProperty(fileNameWatermark: String) {
    var imageFile = File(fileNameWatermark)
    var imageWatermark = ImageIO.read(imageFile)
    transparencyWatermark = imageWatermark.transparency
    if (transparencyWatermark == 3){
        println("Do you want to use the watermark's Alpha channel?")
    }
    else println("Do you want to set a transparency color?")
}

fun positionMethodInput() {
    println("Choose the position method (single, grid):")
}

fun position() {
    DiffX = widthImagePos - widthWatermarkPos
    DiffY = heightImagePos - heightWatermarkPos
    println("Input the watermark position ([x 0-$DiffX] [y 0-$DiffY]):")
}

fun positionCheck(position: MutableList<String>) {
    if ((position[0].toIntOrNull() == null) && (position[0] != "top-left" && position[0] != "top-right" && position[0] != "bottom-left" && position[0] != "bottom-right" && position[0] != "middle")) {
        println("The position input is invalid.")
        exitProcess(0)
    }
    if (position.lastIndex == 1 && (position[0].toIntOrNull() !== null) && (position[0].toInt() < 0 || position[0].toInt() > DiffX || position[1].toInt() < 0 || position[1].toInt() > DiffY)) {
        println("The position input is out of range.")
        exitProcess(0)
    }
}

fun positionCoordinates(position: MutableList<String>) {
    if (position.lastIndex == 0) {
        when {
            position[0] == "top-left" -> {
                xPosition = 0
                yPosition = 0
            }
            position[0] == "top-right" -> {
                xPosition = widthImagePos - widthWatermarkPos
                yPosition = 0
            }
            position[0] == "bottom-left" -> {
                xPosition = 0
                yPosition = heightImagePos - heightWatermarkPos
            }
            position[0] == "bottom-right" -> {
                xPosition = widthImagePos - widthWatermarkPos
                yPosition = heightImagePos - heightWatermarkPos
            }
            position[0] == "middle" -> {
                xPosition = widthImagePos / 2 - widthWatermarkPos / 2
                yPosition = heightImagePos / 2 - heightWatermarkPos / 2
            }
        }
    }
    else {
        xPosition = position[0].toInt()

        yPosition = position[1].toInt()
    }
}


fun positionMethodCheck(positionMethod: String) {
    if ("single" !in positionMethod && "grid" !in positionMethod) {
        println("The position method input is invalid.")
        exitProcess(0)
    }
}

fun outputFileName() {
    println("Input the output image filename (jpg or png extension):")
}

fun checkExtension(outputFileName: String) {
    if ( jpg !in outputFileName && png !in outputFileName) {
        println("The output file extension isn't \"jpg\" or \"png\".")
        exitProcess(0)
    }
}

fun TransparencyColorInput(wishTransparencyColor: String) {
    if (wishTransparencyColor == "yes") println("Input a transparency color ([Red] [Green] [Blue]):")
}

fun checkTransparencyColor(colorsInput: MutableList<String>) {
    if (colorsInput.lastIndex == 2)  {
        val r = colorsInput[0].toInt()
        val g = colorsInput[1].toInt()
        val b = colorsInput[2].toInt()
        if ((r < 0 || r > 255) || (g < 0 || g > 255) || (b < 0 || b > 255)) {
            println("The transparency color input is invalid.")
            exitProcess(0)
        }
    }
    else {
        println("The transparency color input is invalid.")
        exitProcess(0)
    }
}

fun blending(imageName: String, watermarkName: String, outputFileName: String, weight: Int, wishTransparency: String, wishTransparencyColor: String, Red: Int, Green: Int, Blue: Int, positionMethod: String, xPosition: Int, yPosition: Int) {
    var imageFile = File(imageName)
    var image = ImageIO.read(imageFile)
    var watermarkFile = File(watermarkName)
    var watermark = ImageIO.read(watermarkFile)
    var outputFile = File(outputFileName)
    var output = BufferedImage(widthImagePos, heightImagePos, BufferedImage.TYPE_INT_RGB)

    var sumWidth = 0
    var sumHeight = 0
    var maxHorizontal = 0
    var maxVertical = 0
    var gridX = 0
    var gridY = 0
    var gridXMax = 0
    var gridYMax = 0

    while (sumWidth <  widthImagePos) {
        sumWidth += widthWatermarkPos
        maxHorizontal++
    }
    while (sumHeight <  heightImagePos) {
        sumHeight += heightWatermarkPos
        maxVertical++
    }
    if (positionMethod == "grid") {
        repeat(maxVertical) {
            repeat(maxHorizontal){
                gridXMax = gridX + widthWatermarkPos
                if (gridXMax > widthImagePos) gridXMax = widthImagePos
                gridYMax = gridY + heightWatermarkPos
                if (gridYMax > heightImagePos) gridYMax = heightImagePos
                for (x in gridX until gridXMax) {
                    for (y in gridY until gridYMax) {
                        val i = Color(image.getRGB(x, y))
                        val w = Color(watermark.getRGB((x - gridX), (y - gridY)), true)
                        if (w.alpha == 0 && wishTransparency == "yes"){
                            val color = Color(
                                (i.red),
                                (i.green),
                                (i.blue)
                            )
                            output.setRGB(x, y, color.rgb)
                        }
                        else if(wishTransparencyColor == "yes") {
                            if (w.red == Red && w.blue == Blue && w.green == Green) {
                                val color = Color(
                                    (i.red),
                                    (i.green),
                                    (i.blue)
                                )
                                output.setRGB(x, y, color.rgb)
                            }
                            else {
                                val color = Color(
                                    (weight * w.red + (100 - weight) * i.red) / 100,
                                    (weight * w.green + (100 - weight) * i.green) / 100,
                                    (weight * w.blue + (100 - weight) * i.blue) / 100
                                )
                                output.setRGB(x, y, color.rgb)
                            }
                        }
                        else {
                            val color = Color(
                                (weight * w.red + (100 - weight) * i.red) / 100,
                                (weight * w.green + (100 - weight) * i.green) / 100,
                                (weight * w.blue + (100 - weight) * i.blue) / 100
                            )
                            output.setRGB(x, y, color.rgb)
                        }
                    }
                }
                gridX += widthWatermarkPos
            }
            gridX = 0
            gridY += heightWatermarkPos
        }
    }
    if (positionMethod == "single") {
        for (x in 0 until widthImagePos) {
            for (y in 0 until heightImagePos){
                if (x >= xPosition && (x < (xPosition + widthWatermarkPos)) && y >= yPosition && (y < (yPosition + heightWatermarkPos))) {
                    val i = Color(image.getRGB(x, y))
                    val wat = Color(watermark.getRGB(x-xPosition, y-yPosition), true)
                    if (wat.alpha == 0 && wishTransparency == "yes") {
                        val color = Color(
                            (i.red),
                            (i.green),
                            (i.blue)
                        )
                        output.setRGB(x, y, color.rgb)
                    }
                    else if (wishTransparencyColor == "yes") {
                        if (wat.red == Red && wat.blue == Blue && wat.green == Green) {
                            val color = Color(
                                (i.red),
                                (i.green),
                                (i.blue)
                            )
                            output.setRGB(x, y, color.rgb)
                        }
                        else {
                            val color = Color(
                                (weight * wat.red + (100 - weight) * i.red) / 100,
                                (weight * wat.green + (100 - weight) * i.green) / 100,
                                (weight * wat.blue + (100 - weight) * i.blue) / 100
                            )
                            output.setRGB(x, y, color.rgb)
                        }
                    }
                    else {
                        val color = Color(
                            (weight * wat.red + (100 - weight) * i.red) / 100,
                            (weight * wat.green + (100 - weight) * i.green) / 100,
                            (weight * wat.blue + (100 - weight) * i.blue) / 100
                        )
                        output.setRGB(x, y, color.rgb)
                    }
                }
                else {
                    val i = Color(image.getRGB(x, y))
                    val color = Color(i.rgb)
                    output.setRGB(x, y, color.rgb)
                }
            }
        }
    }
    if (png in outputFileName) extension = "png" else extension = jpg
    ImageIO.write(output, extension, outputFile)
    println("The watermarked image $outputFileName has been created.")
}

fun main() {

    fileName()

    var fileName = readln()

    fileNameExists(fileName)

    sizeImage(fileName)

    var wishTransparency = ""
    var wishTransparencyColor = ""

    fileNameWatermark()

    var fileNameWatermark = readln()

    fileNameWatermarkExists(fileNameWatermark)

    transparencyProperty(fileNameWatermark)

    if (transparencyWatermark == 3) {wishTransparency = readln()}
    else {wishTransparencyColor = readln()}

    var red = 0
    var green = 0
    var blue = 0

    if (wishTransparencyColor == "yes") {
        TransparencyColorInput(wishTransparencyColor)

        var colorsInput = readln().split(" ").toMutableList()

        checkTransparencyColor(colorsInput)

        red = colorsInput[0].toInt()
        green = colorsInput[1].toInt()
        blue = colorsInput[2].toInt()
    }

    var Red = red
    var Green = green
    var Blue = blue

    transparency()

    var transparencyDb = readln().toDouble()

    checkTransparency(transparencyDb)

    positionMethodInput()

    var positionMethod = readln()

    positionMethodCheck(positionMethod)

    if (positionMethod == "single") {
        position()

        var position = readln().split(" ").toMutableList()

        positionCheck(position)

        positionCoordinates(position)
    }

    outputFileName()

    var outputFileName = readln()

    checkExtension(outputFileName)

    blending(fileName, fileNameWatermark, outputFileName, transparency, wishTransparency, wishTransparencyColor, Red, Green, Blue, positionMethod, xPosition, yPosition)
}
