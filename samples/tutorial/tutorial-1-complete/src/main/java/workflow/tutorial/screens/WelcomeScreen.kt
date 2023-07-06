package workflow.tutorial.screens

data class WelcomeScreen(
  /** The current name that has been entered. */
  val username: String,
  /** Callback when the name changes in the UI. */
  val onUsernameChanged: (String) -> Unit,
  /** Callback when the login button is tapped. */
  val onLoginTapped: () -> Unit
)
