package com.savestudents.components.appbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.MaterialToolbar
import com.savestudents.components.R
import com.savestudents.components.databinding.AppBarCustomBinding

class AppBarCustomView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding: AppBarCustomBinding = AppBarCustomBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    var title: String = ""
        set(value) {
            field = value
            binding.topAppBar.title = value
        }

    var withNotification: Boolean = false
        set(value) {
            field = value
            if (value) {
                setToolbarWithNotification()
            } else {
                setNavigation()
                binding.topAppBar.title = title
            }
        }

    var root: MaterialToolbar = binding.topAppBar

    init {
        setLayout(attrs)
    }

    private fun setLayout(attr: AttributeSet?) {
        attr?.let { attributeSet: AttributeSet ->
            val attributes =
                context.obtainStyledAttributes(attributeSet, R.styleable.AppBarCustomView)
            withNotification =
                attributes.getBoolean(R.styleable.AppBarCustomView_withNotification, false)
            title = attributes.getString(R.styleable.AppBarCustomView_android_title).orEmpty()
            attributes.recycle()
        }
    }

    private fun setToolbarWithNotification() {
        binding.topAppBar.navigationIcon = getDrawable(context, R.drawable.ic_menu_24)
    }

    private fun setNavigation() {
        binding.topAppBar.navigationIcon = getDrawable(context, R.drawable.ic_arrow_back_24)
    }

}