package workflow.tutorial

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.workflow1.ui.ViewRegistry
import com.squareup.workflow1.ui.WorkflowLayout
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backstack.BackStackContainer
import workflow.tutorial.viewmodels.TutorialViewModel

@OptIn(WorkflowUiExperimentalApi::class)
class TutorialActivity : AppCompatActivity() {

  private val tutorialViewModel: TutorialViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tutorialViewModel.hello()
    setContentView(
      WorkflowLayout(this).apply {
        take(lifecycle, tutorialViewModel.rootRender)
        // start(tutorialViewModel.rootRender, viewRegistry)
      }
    )
  }

}
