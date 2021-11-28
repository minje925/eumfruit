var mapOptions = {
            center: new naver.maps.LatLng(35.83655768685876, 128.72236174435199),
            zoom: 17
        };
        var map = new naver.maps.Map('map', mapOptions);
        var marker = new naver.maps.Marker({
            map: map,
            title: "이음청과",
            position: new naver.maps.LatLng(35.83655768685876, 128.72236174435199)
        });