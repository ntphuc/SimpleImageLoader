package com.line.assignment.simpleimageloader.ui.base

import androidx.lifecycle.ViewModel
import com.line.assignment.simpleimageloader.data.error.mapper.ErrorMapper
import com.line.assignment.simpleimageloader.usecase.errors.ErrorManager


/**
 * Created by PhucNguyen
 */


abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    val errorManager: ErrorManager = ErrorManager(ErrorMapper())
}
