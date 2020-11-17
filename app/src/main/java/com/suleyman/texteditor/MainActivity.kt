package com.suleyman.texteditor

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suleyman.texteditor.adapter.FileListAdapter
import com.suleyman.texteditor.model.FileItem
import java.io.File

class MainActivity : AppCompatActivity() {

    val REQUEST_WRITE_STORAGE = 1001

    val PATH = Environment.getExternalStorageDirectory().absolutePath + File.separator + "Text Editor/"

    val filesList: ArrayList<FileItem> = ArrayList()

    lateinit var rvFilesView: RecyclerView
    lateinit var filesAdapter: FileListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestWritePermissions(this)
        loadFilesInDirectory(filesList)

        rvFilesView = findViewById(R.id.rvFilesView)
        rvFilesView.layoutManager = LinearLayoutManager(this)
        rvFilesView.setHasFixedSize(true)

        filesAdapter = FileListAdapter(filesList)
        

        rvFilesView.adapter = filesAdapter

    }

    private fun loadFilesInDirectory(filesList: ArrayList<FileItem>) {
        val files = File(PATH)

        if (!files.exists()) return

        for (file in files.listFiles()) {
            filesList.add(FileItem(file.name, file.length()))
        }
    }

    private fun createDialogNewFile() {
        val newFileDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        newFileDialog.setTitle("Create")
        newFileDialog.setCancelable(true)
        newFileDialog.setMessage("Input the file name")

        val view: View = layoutInflater.inflate(R.layout.dialog_new_file, null)
        val etFileName: EditText = view.findViewById(R.id.etFileName)

        newFileDialog.setView(view)

        newFileDialog.setPositiveButton("create") { dialog: DialogInterface, i: Int ->
            createNewFile(etFileName)
        }

        newFileDialog.create()
        newFileDialog.show()
    }

    private fun createNewFile(etFileName: EditText) {

        val fileName: String = etFileName.text.toString()

        val newFile = File(PATH + fileName)
        newFile.createNewFile()

        val fileItem = FileItem(newFile.name, newFile.length())

        filesList.add(fileItem)
        filesAdapter.notifyDataSetChanged()

        Toast.makeText(this, "created = ${fileItem.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_WRITE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==  PackageManager.PERMISSION_GRANTED) {
                    createDir()
                }
                return
            }
        }

    }

    private fun requestWritePermissions(context: Activity) {
        val permissions: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val hasPermissions = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (!hasPermissions) {
            ActivityCompat.requestPermissions(context, permissions, REQUEST_WRITE_STORAGE)
        }
    }

    private fun createDir() {
        var file = File(PATH)
        if (!file.exists())
            file.mkdirs()
        Toast.makeText(applicationContext, "CREATED", Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.createFile -> createDialogNewFile()
        }
        return true
    }
}
