@file:Suppress("UnstableApiUsage")

package com.scode.linter

import com.android.SdkConstants
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope.Companion.ALL_RESOURCES_SCOPE
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr

class DimenDetector : LayoutDetector() {

  companion object {
    val ISSUE_GAPS_FOR_MARGIN_PADDING = Issue.create(
      id = "GapsForMarginPadding",
      briefDescription = "Gaps should not be hardcoded",
      explanation = "Gaps should not be hardcoded. Please use the defined value in dls",
      category = Category.CORRECTNESS,
      priority = 5,
      severity = Severity.ERROR,
      implementation = Implementation(DimenDetector::class.java, ALL_RESOURCES_SCOPE)
    )
  }

  override fun getApplicableAttributes(): Collection<String> {
    return listOf(
      SdkConstants.ATTR_LAYOUT_MARGIN,
      SdkConstants.ATTR_LAYOUT_MARGIN_TOP,
      SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM,
      SdkConstants.ATTR_LAYOUT_MARGIN_START,
      SdkConstants.ATTR_LAYOUT_MARGIN_END,
      SdkConstants.ATTR_LAYOUT_MARGIN_LEFT,
      SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT,
    )
  }

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    val matchResult = "([0-9]+)dp".toRegex().matchEntire(attribute.value)
    if (matchResult != null) {
      val (amountDp) = matchResult.destructured
      if (amountDp.toInt() != 0) {
        context.report(
          ISSUE_GAPS_FOR_MARGIN_PADDING,
          context.getLocation(attribute),
          ISSUE_GAPS_FOR_MARGIN_PADDING.getExplanation(TextFormat.TEXT)
        )
      }
    }
  }
}