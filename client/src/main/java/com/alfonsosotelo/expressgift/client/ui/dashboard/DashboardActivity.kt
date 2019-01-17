package com.alfonsosotelo.expressgift.client.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alfonsosotelo.expressgift.client.R
import com.alfonsosotelo.expressgift.client.databinding.ActivityDashboardBinding
import com.alfonsosotelo.expressgift.client.ui.SharedViewModel
import com.alfonsosotelo.expressgift.data.utils.observe
import com.alfonsosotelo.expressgift.data.utils.withViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private var currentController: NavController? = null // Instance here the current NavController

    private val sharedViewModel by lazy { // Instance of SharedViewModel loaded lazily
        withViewModel({ SharedViewModel(application) }) { // Instancing the SharedViewModel with application parameter
            observe(toolbarHandler) {
                binding.toolbarHandler = it
            }
        }
    }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityDashboardBinding>(this, R.layout.activity_dashboard).apply {
            toolbarHandler = this@DashboardActivity.sharedViewModel.toolbarHandler.value
        }

        currentController = findNavController(R.id.navFragments)

        // Set the toolbar with nav controller
        initToolbar()
    }

    // When the user clicks in the back toolbar arrow
    override fun onSupportNavigateUp(): Boolean {
        return currentController?.navigateUp() ?: true
    }

    // When back pressed tell the current NavController to back in his BackStack
    // If is no more fragments in the backstack closes the Activity
    override fun onBackPressed() {
        currentController?.let {
            if (it.popBackStack().not()) {
                finish()
            }
        } ?: run { finish() }
    }

    private fun initToolbar() {
        currentController?.let { toolbar.setupWithNavController(it) }
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.publishMessages(this) // Publish messages in the stack, when the activity is starting
    }

    override fun onStop() {
        // Nearby require to unpublish and unsubscribe everything before close the activity
        sharedViewModel.unpublishMessages(this) // Unpublish messages in the stack, when te activity is stopping
        super.onStop()
    }
}
