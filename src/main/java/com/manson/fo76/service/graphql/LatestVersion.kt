package com.manson.fo76.service.graphql

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class LatestVersion {
    var version: String = ""
    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("version", version)
                .toString()
    }
}