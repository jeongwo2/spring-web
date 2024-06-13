<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Upload with Ajax</title>

<style>
.uploadResult {
    width: 100%;
    background-color: gray;
}

.uploadResult ul {
    display: flex;
    flex-flow: row;
    justify-content: center;
    align-items: center;
}

.uploadResult ul li {
    list-style: none;
    padding: 10px;
}

.uploadResult ul li img {
    width: 100px;
}
</style>

<style>
.bigPictureWrapper {
  position: absolute;
  display: none;
  justify-content: center;
  align-items: center;
  top:0%;
  width:100%;
  height:100%;
  background-color: gray;
  z-index: 100;
}

.bigPicture {
  position: relative;
  display:flex;
  justify-content: center;
  align-items: center;
}
</style>

</head>
<body>
    <h1>Upload with Ajax</h1>

    <div class='bigPictureWrapper'>
        <div class='bigPicture'>
        </div>
    </div>

    <div class='uploadDiv'>
        <input type='file' name='uploadFile' multiple>
    </div>

    <div class='uploadResult'>
        <ul>
        </ul>
    </div>

    <button id='uploadBtn'>Upload</button>

    <script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous">
	</script>

    <!-- Rest of the JavaScript code -->
	<script>
	// Part6 큰 이미지를 표시하는 함수
	function showImage(fileCallPath){
		//alert(fileCallPath);
		$(".bigPictureWrapper").css("display","flex").show();
		$(".bigPicture")
			.html("<img src='/display?fileName="+ encodeURI(fileCallPath)+"'>")
			.animate({width:'100%', height: '100%'}, 1000);
	}
    // Event listener for hiding the large image pop-up window
	$(".bigPictureWrapper").on("click", function(e){
		$(".bigPicture").animate({width:'0%', height: '0%'}, 1000);
		/* 	  setTimeout(() => {
		$(this).hide();
		}, 1000); */

		setTimeout(function() {
			$(".bigPicture").hide();
		}, 1000);
	});

    // 업로드된 파일 목록을 표시할 ul 요소를 선택
	$(".uploadResult").on("click","span", function(e){
		var targetFile = $(this).data("file");
		var type = $(this).data("type");
		console.log(targetFile);

		$.ajax({
			url: '/deleteFile',
			data: {fileName: targetFile, type:type},
			dataType:'text',
			type: 'POST',
			success: function(result){
				alert(result);
			}
		}); //$.ajax
	});

    // 파일 업로드 폼을 초기화
	var cloneObj = $(".uploadDiv").clone();

    // part6 업로드된 파일 목록을 HTML li 요소로 생성
	$("#uploadBtn").on("click", function(e) {
		var formData = new FormData();
		var formData = new FormData();
		var inputFile = $("input[name='uploadFile']");
		var files = inputFile[0].files;
		//console.log(files);

		for (var i = 0; i < files.length; i++) {
			if (!checkExtension(files[i].name, files[i].size)) {
				return false;
			}
			formData.append("uploadFile", files[i]);
		}
        // Part6 jQuery 를 이용한 파일 전송
        // Ajax 를 이용하는 경우에는 FormData 객체를 이용
		$.ajax({
			url : '/uploadAjaxAction',
			processData : false,
			contentType : false,
			data : formData,
			type : 'POST',
			dataType : 'json',
			success : function(result) {
				console.log(result);
				showUploadedFile(result); // 브라우저에서 Ajax 의 처리
				$(".uploadDiv").html(cloneObj.html());
			}
		}); //$.ajax
	});
    // Variable to store the ul element where uploaded file information will be displayed
	var uploadResult = $(".uploadResult ul");

    // Part6 업로드된 파일 목록을 HTML li 요소로 생성
	function showUploadedFile(uploadResultArr){
		var str = "";

		$(uploadResultArr).each(function(i, obj){
            var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
            var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");

			if(!obj.image){
				str += `<li>
                    <div>
                        <a href='/download?fileName=${fileCallPath}'>
                            <img src='/resources/img/attach.png'>
                        </a>
                        ${obj.fileName}
                        <span data-file='${fileCallPath}' data-type='file'> x </span>
                    </div>
                </li>` ;
			}else{
				str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\">"+
					"<img src='display?fileName="+fileCallPath+"'></a>"+
					"<span data-file=\'"+fileCallPath+"\' data-type='image'> x </span>"+
					"<li>";
			}
		});
		uploadResult.append(str);
	}

     // Part6 파일 확장자와 크기를 validate 하는 함수
	function checkExtension(fileName, fileSize) {
	    // Part6 파일의 확장자나 크기 사전처리
        var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	    var maxSize = 5242880; //5MB
		if (fileSize >= maxSize) {
			alert("파일 사이즈 초과");
			return false;
		}

		if (regex.test(fileName)) {
			alert("해당 종류의 파일은 업로드할 수 없습니다.");
			return false;
		}
		return true;
	}

	</script>

</body>
</html>