package com.github.repo.presentation.main.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.repo.databinding.FragmentNotificationsBinding
import com.github.repo.domain.dto.Notification
import com.github.repo.presentation.main.notifications.adapter.NotificationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsFragment : Fragment() {

    lateinit var binding: FragmentNotificationsBinding
    lateinit var rvAdapter: NotificationAdapter
    private val viewModel: NotificationsViewModel by viewModel<NotificationsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getNotifications()
        initView()
    }

    private fun initView() {
        recyclerViewAdapterSetting()
        observeData()
    }

    private fun recyclerViewAdapterSetting() {
        rvAdapter = NotificationAdapter()
        binding.rvNotification.adapter = rvAdapter
    }

    private fun observeData() {
        viewModel.uiState.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Error -> handleError()
                is UiState.Loading -> handleLoading()
                is UiState.GetNotifications -> handleSuccess(state.notificationList)
            }
        }
    }

    private fun handleError() {
        binding.tvError.isVisible = true
        binding.rvNotification.isGone = true
        binding.pbLoading.isGone = true
    }

    private fun handleLoading() {
        binding.pbLoading.isVisible = true
        binding.rvNotification.isGone = true
        binding.tvError.isGone = true
    }

    private fun handleSuccess(list: List<Notification>) {
        rvAdapter.addItemList(list)
        binding.rvNotification.isVisible = true
        binding.pbLoading.isGone = true
        binding.tvError.isGone = true
    }
}