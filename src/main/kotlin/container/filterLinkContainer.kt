package container

import component.*
import data.State
import enums.VisibilityFilter
import hoc.withDisplayName
import org.w3c.dom.events.Event
import react.RClass
import react.RProps
import react.invoke
import react.redux.rConnect
import redux.SetVisibilityFilter
import redux.WrapperAction

interface FilterLinkOwnProps : RProps {
    var title: String
    var filter: VisibilityFilter
}

private interface LinkStateProps : RProps {
    var title: String
    var active: Boolean
}

private interface LinkDispatchProps : RProps {
    var onClick: (Event) -> Unit
}

val filterLinkContainer =
    rConnect<
            State,
            SetVisibilityFilter,
            WrapperAction,
            FilterLinkOwnProps,
            LinkStateProps,
            LinkDispatchProps,
            LinkProps>(
        { state, ownProps ->
            title = ownProps.title
            active = state.filter == ownProps.filter
        },
        { dispatch, ownProps ->
            onClick = { dispatch(SetVisibilityFilter(ownProps.filter)) }
        }
    ) (
        withDisplayName(
            "FilterLink",
            fLink
        ).unsafeCast<RClass<LinkProps>>()
    )