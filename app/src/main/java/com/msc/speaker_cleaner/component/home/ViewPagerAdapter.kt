package com.msc.speaker_cleaner.component.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.adapter.BaseAdapter
import com.msc.speaker_cleaner.databinding.ItemVpBinding
import com.msc.speaker_cleaner.domain.layer.BannerModel

class ViewPagerAdapter : BaseAdapter<BannerModel, ItemVpBinding>() {
    override fun provideViewBinding(parent: ViewGroup): ItemVpBinding {
        return ItemVpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemVpBinding, item: BannerModel) {
        super.binData(viewBinding, item)
        viewBinding.apply {
            ivItemVP.setImageResource(item.imgRes)
            tvItemVP.text = root.context.getText(item.title)
        }
    }
}