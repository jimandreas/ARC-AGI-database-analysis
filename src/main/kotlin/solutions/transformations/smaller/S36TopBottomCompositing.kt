@file:Suppress(
	"RedundantSuppression", "UNUSED_VARIABLE", "MemberVisibilityCanBePrivate", "unused",
	"ReplaceManualRangeWithIndicesCalls", "ReplaceSizeZeroCheckWithIsEmpty",
	"SameParameterValue", "UnnecessaryVariable", "LocalVariableName", "PropertyName",
	"AssignmentInCondition"
)

package solutions.transformations.smaller

import Block
import Point
import solutions.transformations.BidirectionalBaseClass
import solutions.utilities.recreateMatrix
import solutions.utilities.translateBlockBy
import solutions.utilities.translatePointBy

// example: fafffa47 - but it is an inversion.
//   FIRST do a "hypothetical" top bottom compositing

class S36TopBottomCompositing : BidirectionalBaseClass() {
	override val name: String = "top bottom halves compositing"

	var checkedOutput = false
	var numColOutput = 0
	var numRowOutput = 0
	var rowDelta = 0

	var inputRowMidpoint = 0

	var outputColor = 0

	override fun resetState() {
		checkedOutput = false
	}

	// demand same row sizes input to output
	//  demand output is one half number of input columns
	override fun testTransform(): List<List<Int>> {



		if (!checkedOutput) {
			numRowOutput = outputMatrix.size
			numColOutput = outputMatrix[0].size

			val rowCount = inputMatrix.size
			// input matrix must be exactly twice as high as output
			if (numRowOutput * 2 != rowCount ) {
				return emptyList()
			}
			inputRowMidpoint = rowCount / 2

			if (outputBlockList.isNotEmpty()) {
				outputColor = outputBlockList[0].color
			} else if (outputPointList.isNotEmpty()) {
				outputColor = outputPointList[0].color
			} else {
				return emptyList()
			}
			checkedOutput = true
		}

		val bList: MutableList<Block> = mutableListOf()

		for (b in inputBlockList) {
			var translated : Block
			val coordinates = b.coordinates
			val minRow = coordinates.minOf { it.first }
			val maxRow = coordinates.maxOf { it.first }
			val minCol = coordinates.minOf { it.second }
			val maxCol = coordinates.maxOf { it.second }

			if (minRow >= inputRowMidpoint) {
				translated = translateBlockBy(b,  -inputRowMidpoint, 0)
			} else {
				translated = b
			}
			bList.add(translated)
		}

		val pList: MutableList<Point> = mutableListOf()
		// figure out which corner each block is located in
		for (p in inputPointList) {
			var translated : Point
			val coordinate = p.coordinate
			val row = p.coordinate.first

			if (row >= inputRowMidpoint) {
				translated = translatePointBy(p, -inputRowMidpoint, 0)
			} else {
				translated = p
			}
			pList.add(translated)
		}

		val retArray = recreateMatrix(
			numRowOutput, numColOutput,
			bList, pList,
			overrideColors = true,
			colorToUse = outputColor
		)

		return retArray
	}

	override fun returnTestOutput(): List<List<Int>> {
		return testTransform()
	}
}
