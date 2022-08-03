package com.example.lesson16.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.lesson16.R
import com.example.lesson16.databinding.ActivityResultBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class ResultActivity : AppCompatActivity() {
    companion object {
        const val KEY_TRANSMISSION_TEXT = "get_put_txt"

        const val KEY_PARSE_ONE = "parseOne"
        const val KEY_PARSE_TWO = "parseTwo"
        const val KEY_PARSE_THREE = "parseThree"
        const val KEY_PARSE_FOUR = "parseFour"
        const val KEY_PARSE_FIVE = "parseFive"

        const val REGEX_WHITESPACES_CHANGE = """(?:(?!\n)\s)+"""
        val REGEX_PHONE_CHANGE =
            """([\s]?8[\s])(\(0(\d{2})\)[\s])((\d{3}[\-])(\d{2}[\-])(\d{2}))[\s]?""".toRegex()
        val REGEX_CAPS_FOUR_LATTER_WORDS = """\b[а-яА-Яa-zA-Z]{4}\b""".toRegex()
        val REGEX_FOUND_TEXT_IN_TAG =
            """[.*]?(<one>)([а-яА-Я\s0-9a-zA-Z]*)(</one>)[.*]?""".toRegex()
        val REGEX_LINK_CHANGE = """[\s]www\.[A-z0-9]+\.(com|ru)[\s]""".toRegex()

        const val INDEX_GROUP_CODE = 3
        const val INDEX_GROUP_NUMBER = 4
    }

    private var bindingResult: ActivityResultBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingResult = ActivityResultBinding.inflate(layoutInflater)
        setContentView(bindingResult?.root)

        addToolBar()

        bindingResult?.container?.text =
            applyRegexes(intent.extras?.get(KEY_TRANSMISSION_TEXT).toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingResult = null
    }

    private fun addToolBar() {
        val toolBar = bindingResult?.toolBar

        setSupportActionBar(toolBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolBar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun replaceWhitespaces(receivedText: String?): String? {
        return receivedText?.replace(Regex(REGEX_WHITESPACES_CHANGE), "-")
    }

    private fun changeNumberPhone(receivedText: String?): String? {
        val regex = REGEX_PHONE_CHANGE
        val text = receivedText ?: return null

        val modifiedText =
            regex.replace(text) { txtNumber ->
                getModifiedNumberPhone(txtNumber)
            }
        return modifiedText
    }

    private fun getModifiedNumberPhone(txtNumber: MatchResult): String {
        val numberPhone = StringBuilder()
        numberPhone.append(" ${resources.getString(R.string.phone_code)}-")
        numberPhone.append("${txtNumber.groupValues[INDEX_GROUP_CODE]}-")
        numberPhone.append("${txtNumber.groupValues[INDEX_GROUP_NUMBER]} ")
        return numberPhone.toString()
    }

    private fun capsFourLatterWords(receivedText: String?): String? {
        val regex = REGEX_CAPS_FOUR_LATTER_WORDS
        val text = receivedText ?: return null

        val modifiedText =
            regex.replace(text) { word ->
                word.value.uppercase()
            }
        return modifiedText
    }

    private fun foundTextInTag(receivedText: String?): String? {
        val regex = REGEX_FOUND_TEXT_IN_TAG
        val text = receivedText ?: return null

        val modifiedText =
            regex.replace(text) { contentTag ->
                contentTag.groupValues[2]
            }
        return modifiedText
    }

    private fun changeLink(receivedText: String?): String? {
        val regex = REGEX_LINK_CHANGE
        val text = receivedText ?: return null

        val modifiedText =
            regex.replace(text) { link ->
                getModifiedLink(link.value)
            }
        return modifiedText
    }

    private fun getModifiedLink(value: String): String {
        val modifiedLink = StringBuilder()
        modifiedLink.append(" ")
        modifiedLink.append(resources.getString(R.string.txt_http))
        modifiedLink.append(value.trimStart())

        return modifiedLink.toString()
    }

    private fun applyRegexes(
        sourceText: String?
    ): String? {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        var editedText: String? = sourceText

        if (sharedPref.getBoolean(KEY_PARSE_TWO, false)) {
            editedText = changeNumberPhone(editedText)
        }
        if (sharedPref.getBoolean(KEY_PARSE_THREE, false)) {
            editedText = capsFourLatterWords(editedText)
        }
        if (sharedPref.getBoolean(KEY_PARSE_FOUR, false)) {
            editedText = foundTextInTag(editedText)
        }
        if (sharedPref.getBoolean(KEY_PARSE_FIVE, false)) {
            editedText = changeLink(editedText)
        }
        if (sharedPref.getBoolean(KEY_PARSE_ONE, false)) {
            editedText = replaceWhitespaces(editedText)
        }
        return editedText
    }
}