package component

import hoc.withDisplayName
import react.*
import react.dom.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event

interface LinkProps : RProps {
    var title: String
    var active: Boolean
    var onClick: (Event) -> Unit
}

val fLink =
    functionalComponent<LinkProps> {
        button {
            attrs.onClickFunction = it.onClick
            attrs.disabled = it.active
            +it.title
        }
    }

fun RBuilder.link(
    title: String,
    active: Boolean,
    onClick: (Event) -> Unit
) = child(
    withDisplayName("Link", fLink)
) {
    attrs.title = title
    attrs.active = active
    attrs.onClick = onClick
}