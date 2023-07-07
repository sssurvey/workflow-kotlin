package workflow.tutorial.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BackStackScreen
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.flow.StateFlow
import workflow.tutorial.workflows.RootWorkflow

@OptIn(WorkflowUiExperimentalApi::class)
class TutorialViewModel(savedStateHandle: SavedStateHandle): ViewModel() {

  val rootRender: StateFlow<BackStackScreen<Screen>> by lazy {
    renderWorkflowIn(
      RootWorkflow,
      viewModelScope,
      savedStateHandle
    )
  }

  // //TODO: Remove later, just for test
  // val todoEditScreenRender: StateFlow<TodoEditScreen> by lazy {
  //   renderWorkflowIn(
  //     workflow = TodoEditWorkflow,
  //     scope = viewModelScope,
  //     savedStateHandle = savedStateHandle,
  //     prop = EditProps(TodoItem("test title", "test note"))
  //   )
  // }

  // //TODO: Remove later, just for test
  // val welcomeScreenRender: StateFlow<WelcomeScreen> by lazy {
  //   renderWorkflowIn(
  //     workflow = WelcomeWorkflow,
  //     scope = viewModelScope,
  //     savedStateHandle = savedStateHandle
  //   )
  // }
  //
  // //TODO: Remove later, just for test
  // val todoListScreenRender: StateFlow<TodoListScreen> by lazy {
  //   renderWorkflowIn(
  //     workflow = TodoListWorkflow,
  //     scope = viewModelScope,
  //     savedStateHandle = savedStateHandle
  //   )
  // }

  fun hello() {
    Log.d(TAG, "hello called")
  }

  companion object {
    private const val TAG = "TutorialViewModel"
  }

}
