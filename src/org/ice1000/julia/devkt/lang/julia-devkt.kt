package org.ice1000.julia.devkt.lang

import org.ice1000.devkt.openapi.*
import org.ice1000.julia.devkt.lang.psi.JuliaSymbol
import org.ice1000.julia.devkt.lang.psi.JuliaTypes
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.tree.IElementType

class Julia<TextAttributes> : ExtendedDevKtLanguage<TextAttributes>(
		JuliaLanguage.INSTANCE,
		JuliaParserDefinition
) {
	override val lineCommentStart: String get() = "#"
	override fun satisfies(fileName: String) = fileName.endsWith(".jl")
	override fun attributesOf(type: IElementType, colorScheme: ColorScheme<TextAttributes>) = when (type) {
		JuliaTypes.QUOTE_START,
		JuliaTypes.QUOTE_END,
		JuliaTypes.CMD_QUOTE_START,
		JuliaTypes.CMD_QUOTE_END,
		JuliaTypes.TRIPLE_QUOTE_START,
		JuliaTypes.TRIPLE_QUOTE_END,
		JuliaTypes.REGEX_START,
		JuliaTypes.REGEX_END,
		JuliaTypes.REGULAR_STRING_PART_LITERAL -> colorScheme.string
		JuliaTypes.STRING_INTERPOLATE_START,
		JuliaTypes.STRING_INTERPOLATE_END -> colorScheme.interpolation
		JuliaTypes.STRING_ESCAPE,
		JuliaTypes.STRING_UNICODE -> colorScheme.stringEscape
		JuliaTypes.CHAR_LITERAL -> colorScheme.charLiteral
		JuliaTypes.LINE_COMMENT -> colorScheme.lineComments
		JuliaTypes.BLOCK_COMMENT_BODY,
		JuliaTypes.BLOCK_COMMENT_END,
		JuliaTypes.BLOCK_COMMENT_START -> colorScheme.blockComments
		JuliaTypes.INT_LITERAL,
		JuliaTypes.FLOAT_LITERAL -> colorScheme.numbers
		JuliaTypes.SEMICOLON_SYM -> colorScheme.semicolon
		JuliaTypes.FLOAT_CONSTANT -> colorScheme.predefined
		JuliaTypes.MACRO_SYM -> colorScheme.macro
		in JuliaHighlighter.BRACKETS -> colorScheme.parentheses
		in JuliaHighlighter.M_BRACKETS -> colorScheme.brackets
		in JuliaHighlighter.B_BRACKETS -> colorScheme.braces
		in JuliaHighlighter.KEYWORDS_LIST -> colorScheme.keywords
		in JuliaHighlighter.OPERATOR_LIST -> colorScheme.operators
		in JuliaHighlighter.ASSIGNMENT_OPERATOR_LIST -> colorScheme.operators
		in JuliaHighlighter.UNICODE_OPERATOR_LIST -> colorScheme.operators
		else -> super.attributesOf(type, colorScheme)
	}

	override val blockComment = "#=" to "=#"

	override fun annotate(element: PsiElement, document: AnnotationHolder<TextAttributes>, colorScheme: ColorScheme<TextAttributes>) {
		super.annotate(element, document, colorScheme)
		when (element) {
			is JuliaSymbol -> when {
				element.isFunctionName -> document.highlight(element, colorScheme.function)
				element.isVariableName -> document.highlight(element, colorScheme.variable)
			}
		}
	}
}
