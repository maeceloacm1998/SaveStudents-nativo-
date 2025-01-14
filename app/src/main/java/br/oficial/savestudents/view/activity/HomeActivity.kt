package br.oficial.savestudents.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.core.constants.FirestoreDbConstants
import br.oficial.savestudents.controller.HeaderHomeActivityController
import br.oficial.savestudents.controller.HomeActivityController
import br.oficial.savestudents.controller.SearchBarController
import br.oficial.savestudents.databinding.ActivityHomeBinding
import br.oficial.savestudents.debug_mode.view.activity.AllSubjectsListActivity
import br.oficial.savestudents.debug_mode.view.activity.LoginActivity
import com.br.core.service.internal.dao.AdminCheckDAO
import br.oficial.savestudents.viewModel.HomeViewModel
import com.br.core.service.internal.database.AdminCheckDB
import com.example.data_transfer.model.contract.HeaderHomeActivityContract
import com.example.data_transfer.model.contract.HomeActivityContract
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adminCheckDAO: AdminCheckDAO
    private val headerHomeActivityController by lazy { HeaderHomeActivityController(headerContract) }
    private val homeActivityController by lazy { HomeActivityController(homeContract) }
    private val searchBarController by lazy { SearchBarController(homeContract) }
    private val viewModel by lazy { HomeViewModel()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adminCheckDAO = AdminCheckDB.getDataBase(applicationContext).adminCheckDAO()

        fetchSubjectList()
        controllers()
        observers()
        handleFirebaseDynamicLinks(intent)
        handleCheckUser()
        handleClickFaqButtonListener()
    }

    override fun onResume() {
        super.onResume()
        checkAdminEnabled()

        if (isFiltered) {
            handleFiltersSelected()
        }
    }

    override fun onPause() {
        super.onPause()
        isFiltered = false
    }

    private fun controllers() {
        handleHeaderController()
        handleHomeController()
    }

    private fun observers() {
        viewModel.subjectList.observe(this) { observe ->
            setError(false)
            homeActivityController.setSubjectList(observe)
        }

        viewModel.searchList.observe(this) { observe ->
            searchBarController.apply {
                isResponseError(false)
                setLoading(false)
                setSubjectList(observe)
            }
        }

        viewModel.subjectListError.observe(this) { observe ->
            setError(true)
            homeActivityController.setResponseError(observe)
        }

        viewModel.searchError.observe(this) { observe ->
            searchBarController.apply {
                isResponseError(true)
                setResponseError(observe)
            }
        }
    }

    private fun handleFirebaseDynamicLinks(intent: Intent) {
        Firebase.dynamicLinks.getDynamicLink(intent).addOnSuccessListener { dynamicLinkData ->
            if (dynamicLinkData != null) {
                showDynamicLinkOffer(dynamicLinkData.link)
            }
        }.addOnFailureListener(this) { e ->
            Log.d("DynamicLinkError", e.localizedMessage)
        }
    }

    private fun handleCheckUser() {
        val userDB = adminCheckDAO.getAdminModeStatus()
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(
            userDB?.email.toString(),
            userDB?.id.toString()
        )

        user?.reauthenticate(credential)?.addOnFailureListener {
            userDB?.let { it -> adminCheckDAO.deleteAdminModeStatus(it.id) }
            headerHomeActivityController.isAdminMode(false)
        }
    }

    private fun handleClickFaqButtonListener() {
        binding.FAQ.setOnClickListener {
            val intent = FAQActivity.newInstance(applicationContext)
            startActivity(intent)
        }
    }

    private fun checkAdminEnabled() {
        val userDB = adminCheckDAO.getAdminModeStatus()
        if (userDB == null) {
            headerHomeActivityController.isAdminMode(false)
        } else {
            headerHomeActivityController.isAdminMode(true)
        }
    }

    private fun showDynamicLinkOffer(uri: Uri?) {
        val promotionCode = uri?.getQueryParameter("id")
        if (promotionCode.isNullOrBlank().not()) {
            startActivity(
                TimelineActivity.newInstance(
                    applicationContext, promotionCode.toString()
                )
            )
        }
    }

    private fun setError(status: Boolean) {
        homeActivityController.isResponseError(status)
    }

    private fun fetchSubjectList() {
        viewModel.getSubjectList()
    }

    private fun handleHeaderController() {
        binding.headerMainView.apply {
            setController(headerHomeActivityController)
            layoutManager = LinearLayoutManager(context)
            requestModelBuild()
        }
    }

    private fun handleHomeController() {
        binding.homeMainView.apply {
            setController(homeActivityController)
            layoutManager = LinearLayoutManager(context)
            requestModelBuild()
        }
    }

    private fun handleFiltersSelected() {
        homeActivityController.clearSubjectList()

        if (checkboxRadioSelected.isBlank() && checkboxSelectedList.isEmpty()) {
            fetchSubjectList()
        }
        filterSubjectList()
    }

    private fun filterSubjectList() {
        viewModel.filterSubjectList(
            FirestoreDbConstants.Collections.SUBJECTS_LIST,
            checkboxSelectedList,
            checkboxRadioSelected
        )
    }

    private val headerContract = object : HeaderHomeActivityContract {
        override fun clickFilterButtonListener() {
            startActivity(
                FilterOptionsActivity.newInstance(
                    applicationContext, checkboxRadioSelected, checkboxSelectedList.toTypedArray()
                )
            )
        }

        override fun clickSearchBarListener() {
            binding.homeMainView.apply {
                setController(searchBarController)
                layoutManager = LinearLayoutManager(context)
                requestModelBuild()
            }
        }

        override fun clickButtonCancelListener() {
            homeActivityController.clearSubjectList()
            binding.homeMainView.apply {
                setController(homeActivityController)
                layoutManager = LinearLayoutManager(context)
                requestModelBuild()
            }
            fetchSubjectList()
        }

        override fun editTextValue(text: String) {
            searchBarController.setLoading(true)
            viewModel.searchSubjectList(FirestoreDbConstants.Collections.SUBJECTS_LIST, text)
        }

        override fun adminModeOnActiveListener() {
            val intent = AllSubjectsListActivity.newInstance(applicationContext)
            startActivity(intent)
        }

        override fun joinAdminModeListener() {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
    }

    private val homeContract = object : HomeActivityContract {
        override fun tryAgainListener() {
            fetchSubjectList()
        }

        override fun clickHorizontalCardListener(subjectId: String) {
            startActivity(TimelineActivity.newInstance(applicationContext, subjectId))
        }
    }

    companion object {
        var checkboxRadioSelected: String = ""
        var checkboxSelectedList: MutableList<String> = mutableListOf()
        var isFiltered = false

        @JvmStatic
        fun newInstance(
            context: Context,
        ): Intent {
            return Intent(context, HomeActivity::class.java)
        }

        fun saveFiltersSelected(
            checkboxRadioSelected: String, checkboxSelectedList: MutableList<String>
        ) {
            this.checkboxRadioSelected = checkboxRadioSelected
            this.checkboxSelectedList = checkboxSelectedList
        }
    }
}