@file:Suppress("UnstableApiUsage")

package com.scode.linter

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class IssueRegistry: IssueRegistry() {

  override val api: Int = CURRENT_API

  override val issues: List<Issue>
    get() = listOf(
      DimenDetector.ISSUE_GAPS_FOR_MARGIN_PADDING,
      IdDetector.ISSUE_ID_LAYOUT
    )

  override val vendor: Vendor = Vendor(
    vendorName = "Android Open Source Project",
    feedbackUrl = "https://github.com/googlesamples/android-custom-lint-rules/issues",
    contact = "https://github.com/googlesamples/android-custom-lint-rules"
  )
}
