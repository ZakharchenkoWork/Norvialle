package com.hast.norvialle.presenataion.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

/**
 * Created by Konstantyn Zakharchenko on 16.12.2019.
 */
class GridLayoutRightToLeftManager(context : Context, spanCount: Int ) : GridLayoutManager(context, spanCount){
    override fun isLayoutRTL(): Boolean {
        return true
    }
}