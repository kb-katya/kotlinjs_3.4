package container

import component.*
import data.*
import enums.VisibilityFilter
import hoc.withDisplayName
import org.w3c.dom.events.Event
import react.*
import redux.*
import react.redux.rConnect

interface AnyFullDispatchProps : RProps {
    var onClick: (Int) -> (Event) -> Unit
}

interface AnyFullStateProps<O, S> : RProps {
    var subobjs: Map<Int, S>
    var presents: Map<Int, Boolean>?
}

interface AnyFullOwnProps<O> : RProps {
    var obj: Pair<Int, O>
}

private fun <O> getVisibleAny(objects: Map<Int, O>, presents: Map<Int, Boolean>?, filter: VisibilityFilter): Map<Int, O> = when (filter) {
    VisibilityFilter.SHOW_ALL -> objects
    VisibilityFilter.SHOW_PRESENT -> objects.filter {
        presents?.get(it.key) ?: false
    }
    VisibilityFilter.SHOW_UNPRESENT -> objects.filter {
        !(presents?.get(it.key) ?: true)
    }
}

val lessonFullContainer =
    rConnect<
            State,
            RAction,
            WrapperAction,
            AnyFullOwnProps<Lesson>,
            AnyFullStateProps<Lesson, Student>,
            AnyFullDispatchProps,
            AnyFullProps<Lesson, Student>>(
        { state, ownProps ->
            subobjs = getVisibleAny(state.students, state.presents[ownProps.obj.first], state.filter)
            presents = state.presents[ownProps.obj.first]
        },
        { dispatch, ownProps ->
            onClick =
                { index ->
                    {
                        dispatch(ChangePresent(ownProps.obj.first, index))
                    }
                }
        }
    )(
        withDisplayName(
            "LessonFull",
            fAnyFull<Lesson, Student>(RBuilder::student)
        )
            .unsafeCast<RClass<AnyFullProps<Lesson, Student>>>()
    )

val studentFullContainer =
    rConnect<
            State,
            RAction,
            WrapperAction,
            AnyFullOwnProps<Student>,
            AnyFullStateProps<Student, Lesson>,
            AnyFullDispatchProps,
            AnyFullProps<Student, Lesson>>(
        { state, ownProps ->
            subobjs = getVisibleAny(state.lessons, state.presentsStudent(ownProps.obj.first), state.filter)
            presents = state.presentsStudent(ownProps.obj.first)
        },
        { dispatch, ownProps ->
            onClick = { index ->
                {
                    dispatch(ChangePresent(index, ownProps.obj.first))
                }
            }
        }
    )(
        withDisplayName(
            "StudentFull",
            fAnyFull<Student, Lesson>(RBuilder::lesson)
        )
            .unsafeCast<RClass<AnyFullProps<Student, Lesson>>>()
    )