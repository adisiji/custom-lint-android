@file:Suppress("UnstableApiUsage")

package com.scode.linter

import com.android.SdkConstants
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import com.android.tools.lint.detector.api.XmlContext
import com.android.utils.forEach
import org.w3c.dom.Attr
import org.w3c.dom.Element

class IdDetector : LayoutDetector() {

  private var abbreviationElement = ""

  companion object {
    val ISSUE_ID_LAYOUT = Issue.create(
      id = "IssueIdLayout",
      briefDescription = "Id should be defined with prefix of the element",
      explanation = "Id should be defined with prefix of the element to differ with other element",
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(IdDetector::class.java, Scope.ALL_RESOURCES_SCOPE)
    )
  }

  override fun getApplicableElements(): Collection<String> {
    return listOf(
      SdkConstants.EDIT_TEXT,
      SdkConstants.TEXT_VIEW
    )
  }

  override fun visitElement(context: XmlContext, element: Element) {
    for (i in 0 until element.localName.length) {
      val c = element.localName[i]
      abbreviationElement += if (Character.isUpperCase(c)) "$c" else ""
    }
    abbreviationElement = abbreviationElement.lowercase()
  }

  override fun getApplicableAttributes(): Collection<String> {
    return listOf(
      SdkConstants.ATTR_ID
    )
  }

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    val matchResult = "@\\+id/([A-Za-z]+)".toRegex().matchEntire(attribute.value)
    if (matchResult != null && abbreviationElement.isNotEmpty()) {
      val id = matchResult.destructured
      if(!id.toString().startsWith(abbreviationElement)) {
        context.report(
          ISSUE_ID_LAYOUT,
          context.getLocation(attribute),
          ISSUE_ID_LAYOUT.getExplanation(TextFormat.TEXT)
        )
      }
    }
  }
}