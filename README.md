# Deta4j
https://deta.sh | https://deta.space API in java 😳  
WIP, see [Deta Base est class](https://github.com/UwUDev/Deta4j/blob/master/src/main/java/me/uwu/deta4j/base/test/) and [Deta Drive est class](https://github.com/UwUDev/Deta4j/blob/master/src/main/java/me/uwu/deta4j/drive/test) for having an idea
- [Discord](https://discord.gg/deta-827546555200438332)
- [GitHub](https://github.com/UwUDev)
# Table of contents
- [Installation](#installation)
  - [Maven](#maven)
  - [Gradle](#gradle)
- [Usage](#usage)
    - [Deta Base](#deta-base)
        - [Create a Deta Bases instance and get any other Deta Base instance](#create-a-deta-bases-instance-and-get-any-other-deta-base-instance)
        - [Insert an element](#insert-an-element)
        - [Get an element with key and serialize it](#get-an-element-with-key-and-serialize-it)
        - [ResponseItem usage](#responseitem-usage)
        - [Quering](#quering)
    - [Deta Drive](#deta-drive)
        - [Create a Deta Drives instance and get any other Deta Drive instance](#create-a-deta-drives-instance-and-get-any-other-deta-drive-instance)
        - [Upload a file](#upload-a-file)
        - [Search files](#search-files)
        - [Delete a file](#delete-a-file)
        - [Download a file](#download-a-file)
        - [Export a file](#export-a-file)
        - [FileResponse usage](#fileresponse-usage)
        - [PackedFilesResponse usage](#packedfilesresponse-usage)
# Installation
## Maven
Soon :tm:
## Gradle
Soon :tm:
# Usage
## Deta Base
### Create a Deta Bases instance and get any other Deta Base instance
```java
DetaBases bases = new DetaBases("projectKey");
DetaBase base = bases.get("baseName");
```
### Insert an element
```java
TestObject testObject = new TestObject("Super test value");

ResponseItem responseItem = base.insert(testObject); // any object expected JsonObject
ResponseItem responseItem = base.insert(testObject, "superCoolCustomKey");

JsonObject jsonObject = responseItem.getJsonObject();
jsonObject.addProperty("test", "test");
ResponseItem responseItem = base.insert((Object) jsonObject);
```
### Get an element with key and serialize it
```java
Optional<ResponseItem> item = base.getItemByKey("69");
if(item.isPresent()){
    TestObject testObject = item.get().get(TestObject.class);
} else {
    // item not found
}
```
### ResponseItem usage
```java
ResponseItem responseItem = base.insert(testObject);
//or
ResponseItem responseItem = base.getItemByKey("69").get();


// detele item in db
responseItem.delete();


//clone item in db
responseItem.reinsert(); // return new ResponseItem
//or
responseItem.reinsert("customNewKey"); // return new ResponseItem


//update item in db
TestObject testObject = new TestObject("Super test value v2");
responseItem.update(testObject); // return new ResponseItem
```
### Quering
```java
// get all items
QueryResponse queryResponse = base.query(); // return first elements, note that you can't get all elements with this method if there is more than 1000 elements

// get next elements if there is more than 1000 elements
QueryResponse queryResponse = queryResponse.next(); // return next QueryResponse if there is more or null, also you can add a limit to the query

// basic options
QueryResponse queryResponse = base.query(10); // return first 10 elements
QueryResponse queryResponse = base.query("key"); // return first elements after this key
QueryResponse queryResponse = base.query(10, "key"); // return first 10 elements after this key

// advanced options
QueryOperator query = new LessThanOrEqualQuery("test", 1); // return all elements where test <= 1
QueryOperator query2 = new ContainsQuery("name", "Ja"); // return all elements where name contains "Ja"
QueryOperator query3 = new StartWithQuery("id", "123"); // return all elements where id start with "123"
QueryOperator[] queries = new QueryOperator[]{query, query2, query3};
QueryOperator query4 = new AndQuery(queries); // query1 AND query2 AND query3
QueryOperator query5 = new OrQuery(queries); // query1 OR query2 OR query3
QueryResponse queryResponse = base.query(query4); // return first elements where query4 is true
QueryResponse queryResponse = base.query(query5, 10); // return first 10 elements where query5 is true
```
see all operators [here](https://github.com/UwUDev/Deta4j/tree/master/src/main/java/me/uwu/deta4j/base/query/impl)
## Deta Drive
### Create a Deta Drives instance and get any other Deta Drive instance
```java
DetaDrives drives = new DetaDrives("projectKey");
DetaDrive drive = drives.get("driveName");
```
### Upload a file
```java
File reallyCoolFile = new File("uploadMe.txt");
Optional<FileResponse> fileResponse = drive.upload(reallyCoolFile);
if (fileResponse.isPresent()) {
    // file uploaded
} else {
    // file not uploaded
}
```
### Search files
```java
PackedFilesResponse packedFilesResponse = drive.search(); // return all files
PackedFilesResponse packedFilesResponse = drive.search(10, "prefix", "lastFileName"); // return 10 files after "lastFileName" with prefix "prefix"
PackedFilesResponse packedFilesResponse = drive.search(10); // return 10 first files
//there is more options, see the class
```
### Delete a file
```java
String[] deleted = drive.delete("fileName"); // return deleted files
//or
String[] deleted = drive.delete("fileName", "fileName2"); // return deleted files
//or
String[] toRemove = new String[]{"fileName", "fileName2", "fileName3"...};
String[] deleted = drive.delete(toRemove); // return deleted files
```
### Download a file
```java
byte[] bytes = drive.download("fileName"); // return file bytes
```
### Export a file
```java
File outputFile = new File("outputFile.txt");
File exportedFile = drive.export("fileName", outputFile); // return destination file or null if file not found
```
### FileResponse usage
```java
FileResponse fileResponse = drive.upload(reallyCoolFile).get();
//or
FileResponse fileResponse = drive.search().getFiles().get(0);


//delete file
fileResponse.delete();


//download file
byte[] bytes = fileResponse.download();


//export file
File outputFile = new File("outputFile.txt");
File exportedFile = fileResponse.export(outputFile);
```
### PackedFilesResponse usage
```java
PackedFilesResponse packedFilesResponse = drive.search();

//get files
List<FileResponse> files = packedFilesResponse.getFiles();

//get last file name
String nextFileName = packedFilesResponse.getPaging().getLast();

//get files count
int count = packedFilesResponse.getPaging().getSize();

//get next files
PackedFilesResponse nextPackedFilesResponse = packedFilesResponse.next();
//or
PackedFilesResponse nextPackedFilesResponse = packedFilesResponse.next(10); // return next 10 files
```
