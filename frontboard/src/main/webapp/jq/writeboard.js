$(function () { 	
	//--이미지첨부파일 변경될때  미리보기 START--
	$('div.write>form>div.data>input[name=imageFile]').change(function(){
		let file = this.files[0];
		$("div.image>img.preview").attr('src',URL.createObjectURL(file));
	});
	//--이미지첨부파일 변경될때  미리보기 END--
	
	
	//--글쓰기 버튼 클릭 START--
	let $btObj = $('div.write form input[type=button]');	
	$btObj.click(function () {
		let $writeFormObj = $("div.write form");
		let formData = new FormData($writeFormObj[0]);
		
		let obj = {};
		obj["fr"] = "bonjour";
		obj["en"] = "hello";
		obj["ko"] = "안녕하세요";
		//formData.append("greeting", new Blob([ JSON.stringify(obj) ], {type : "application/json"}));
		formData.append("greeting", JSON.stringify(obj)); //강제로 입력해서 테스트해봄 (이름, 값)
		//formData에는 객체를 저장할 수 없음 
		//JSON.stringify -> JS객체를 JSON문자열로 변환해줌 
		
		formData.forEach(function (value, key) {
			console.log(key + ":" + value);
		});
		
		// let obj2 = formData.get("greeting"); //특정 key에 만족하는 value만 검색 
		// console.log(obj2);
		let obj2 = formData.get("imageFile");
		console.log(obj2);
		$.ajax({
			url: '/backboard/writeboard',
			method: "post",
			processData: false, //파일업로드용 설정
			contentType: false, //파일업로드용 설정
			data: formData, //파일업로드용 설정
			
			cache:false, //이미지 다운로드용 설정
	        xhrFields:{  //이미지 다운로드용 설정
	            responseType: 'blob'
	        }, 
			success: function (responseData) {
				let $img = $('div.image>img.downloadview');
				let url = URL.createObjectURL(responseData);
				$img.attr('src', url); 
			},
			error: function (jqXHR, textStatus) {//응답실패
				alert("에러:" + jqXHR.status);
			}
		});
		return false;
	});
	//--글쓰기 버튼 클릭 END--
});