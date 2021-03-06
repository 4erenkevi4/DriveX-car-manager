package com.example.drivex.presentation.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.presentation.adapters.MainAdapter
import com.example.drivex.presentation.ui.activity.FuelActivity
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.utils.Constans.REQUEST_CODE_LOCATION_PERMISSION
import com.example.drivex.utils.TrackingUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AbstractViewModel
    lateinit var allExpenses: TextView
    lateinit var allMileage: TextView
    lateinit var allVolume: TextView
    lateinit var allCostFuel: TextView
    lateinit var allCostService: TextView
    lateinit var liveDataCost: LiveData<String>
    lateinit var liveDataMileage: LiveData<String>
    lateinit var liveDataCostFUel: LiveData<String>
    lateinit var liveDataVolumeFUel: LiveData<String>
    lateinit var liveDataCostService: LiveData<String>
    lateinit var liveDatarefuelSum: LiveData<Int>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater
            .inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        allExpenses = view.findViewById(R.id.all_expencess_car)
        allMileage = view.findViewById(R.id.text_mileage_info)
        allVolume = view.findViewById(R.id.volume_summ)
        allCostFuel = view.findViewById(R.id.fuel_expenses)
        allCostService = view.findViewById(R.id.service_summ)
        liveDataCost = viewModel.allExpensesSum
        liveDataMileage = viewModel.lastMileageStr
        liveDataCostFUel = viewModel.allFuelCostSum
        liveDataVolumeFUel = viewModel.allFuelVolumeSum
        liveDataCostService = viewModel.allServiceCostSum
        liveDatarefuelSum = viewModel.refuelSum
        setinfoView()
        setRecyclerview(view)
        requestPermissions()
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setinfoView() {
        liveDataCost.observe(viewLifecycleOwner, { allExpenses.text = "?????????? ??????????????: $it" })
        liveDataMileage.observe(viewLifecycleOwner, { allMileage.text = "????????????: $it" })
        liveDatarefuelSum.observe(viewLifecycleOwner,
            { allCostFuel.text = "?????????? ?????????????? ???? ??????????????: $it BYN" })
        liveDataVolumeFUel.observe(viewLifecycleOwner,
            { allVolume.text = "?????????? ?????????? ??????????????: $it" })
        liveDataCostService.observe(viewLifecycleOwner,
            { allCostService.text = "?????????? ?????????????? ???? ????????????: $it" })


    }

    private fun setRecyclerview(view: View) {
        val adapter = MainAdapter { id ->
            startActivity(Intent(context, FuelActivity::class.java).putExtra("id", id))
        }
        recyclerView = view.findViewById(R.id.recycler_view_home)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        GlobalScope.launch(Dispatchers.Default) {
            adapter.setData(viewModel.readAllDataByDate())
        }
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).setThemeResId(R.style.Base_Theme_AppCompat_Dialog_Alert).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}