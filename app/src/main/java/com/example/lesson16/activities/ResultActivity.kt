package com.example.lesson16.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.lesson16.KEY_TRANSMISSION_TEXT
import com.example.lesson16.R
import com.example.lesson16.databinding.ActivityResultBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class ResultActivity : AppCompatActivity() {
    companion object {
        const val KEY_PARSE_ONE = "parseOne"
        const val KEY_PARSE_TWO = "parseTwo"
        const val KEY_PARSE_THREE = "parseThree"
        const val KEY_PARSE_FOUR = "parseFour"
        const val KEY_PARSE_FIVE = "parseFive"
    }

    private var bindingResult: ActivityResultBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingResult = ActivityResultBinding.inflate(layoutInflater)
        setContentView(bindingResult?.root)

        addToolBar()

        bindingResult?.container?.text = modifiedTxtByCheckBox()
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

    private fun parseOneReplaceSpaces(receivedText: String?): String? {
        return receivedText?.replace(Regex("(?:(?!\\n)\\s)+"), "-")
    }

    private fun parseTwoChangeNumberPhone(receivedText: String?): String? {
        val regex =
            "([\\s]?8[\\s])(\\(0\\d{2}\\)[\\s])(\\d{3}[\\-])(\\d{2}[\\-])(\\d{2})[\\s]?".toRegex()
        val modifiedText = receivedText?.let {
            regex.replace(it) { txtNumber ->
                getModifiedNumberPhone(txtNumber)
            }
        }
        return modifiedText
    }

    private fun parseThreeCapsLkFourLatterWords(receivedText: String?): String? {
        val regex = "\\b[а-яА-Яa-zA-Z]{4}\\b".toRegex()
        val modifiedText = receivedText?.let {
            regex.replace(it) { word ->
                word.value.uppercase()
            }
        }
        return modifiedText
    }

    private fun parseFourFindTxtInTag(receivedText: String?): String? {
        val regex = "[.*]?(<one>)([а-яА-Я\\s0-9a-zA-Z]*)(</one>)[.*]?".toRegex()
        val modifiedText = receivedText?.let {
            regex.replace(it) { contentTag ->
                getTagContent(contentTag.value)
            }
        }
        return modifiedText
    }

    private fun parseFiveChangeLink(receivedText: String?): String? {
        val regex = "[\\s]www\\.[A-z0-9]+\\.(com|ru)[\\s]".toRegex()
        val modifiedText = receivedText?.let {
            regex.replace(it) { link ->
                getModifiedLink(link.value)
            }
        }
        return modifiedText
    }

    private fun modifiedTxtByCheckBox(): String? {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        val sourceText = intent.extras?.get(KEY_TRANSMISSION_TEXT).toString()
        var editedText: String? = sourceText

        if (sharedPref.getBoolean(KEY_PARSE_TWO, false)) {
            editedText = parseTwoChangeNumberPhone(editedText)
        }
        if (sharedPref.getBoolean(KEY_PARSE_THREE, false)) {
            editedText = parseThreeCapsLkFourLatterWords(editedText)
        }
        if (sharedPref.getBoolean(KEY_PARSE_FOUR, false)) {
            editedText = parseFourFindTxtInTag(editedText)
        }
        if (sharedPref.getBoolean(KEY_PARSE_FIVE, false)) {
            editedText = parseFiveChangeLink(editedText)
        }
        if (sharedPref.getBoolean(KEY_PARSE_ONE, false)) {
            editedText = parseOneReplaceSpaces(editedText)
        }
        return editedText
    }

    private fun getModifiedNumberPhone(txtNumber: MatchResult): String {
        val beginCodePhone = 4
        val endCodePhone = 6

        val beginNumberPhone = 8
        val endNumberPhone = 17

        val numberPhone = StringBuilder()
        numberPhone.append(" ${resources.getString(R.string.phone_code)}-")
        numberPhone.append(
            "${
                txtNumber.value.trimStart().substring(beginCodePhone, endCodePhone)
            }-"
        )
        numberPhone.append(
            "${
                txtNumber.value.trimStart().substring(beginNumberPhone, endNumberPhone)
            } "
        )
        return numberPhone.toString()
    }

    private fun getTagContent(txt: String): String {
        val pattern: Pattern = Pattern.compile("[.*]?(<one>)([а-яА-Я\\s]*)(</one>)[.*]?")
        val matcher: Matcher = pattern.matcher(txt)
        var content = ""
        if (matcher.find()) {
            content = matcher.group(2)
        }
        return content
    }

    private fun getModifiedLink(value: String): String {
        val modifiedLink = StringBuilder()
        modifiedLink.append(" ")
        modifiedLink.append(resources.getString(R.string.txt_http))
        modifiedLink.append(value.trimStart())

        return modifiedLink.toString()
    }
}