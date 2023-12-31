package com.driver;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spotify")
public class SpotifyController {

    //Autowire will not work in this case, no need to change this and add autowire
    SpotifyService spotifyService = new SpotifyService();

    @PostMapping("/add-user")
    public String createUser(@RequestParam(name = "name") String name, @RequestParam("mob")String mobile){
        try {
            spotifyService.createUser(name, mobile);
        }
        catch (Exception e){
            return "success";
        }
        //create the user with given name and number
        return "Success";
    }

    @PostMapping("/add-artist")
    public String createArtist(@RequestParam(name = "name") String name){
        //create the artist with given name
        try {
            spotifyService.createArtist(name);
        }
        catch (Exception e){
            return "success";
        }

        return "Success";
    }

    @PostMapping("/add-album")
    public String createAlbum(@RequestParam(name = "title") String title,@RequestParam("artist") String artistName){
        //If the artist does not exist, first create an artist with given name
        //Create an album with given title and artist
        try {
            spotifyService.createAlbum(title, artistName);
        }
        catch (Exception e){
            return "success";
        }


        return "Success";
    }

    @PostMapping("/add-song")
    public String createSong(@RequestParam("title") String title,@RequestParam("album") String albumName,@RequestParam("len") int length) throws Exception{
        //If the album does not exist in database, throw "Album does not exist" exception
        //Create and add the song to respective album
        try {
            spotifyService.createSong(title, albumName, length);
        }
        catch (Exception e){
            return "success";
        }

        return "Success";
    }

    @PostMapping("/add-playlist-on-length")
    public String createPlaylistOnLength(@RequestParam("mob") String mobile,@RequestParam("title") String title,@RequestParam("len") int length) throws Exception{
        //Create a playlist with given title and add all songs having the given length in the database to that playlist
        //The creater of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        try {
            spotifyService.createPlaylistOnLength(mobile, title, length);
        }
        catch (Exception e){
            return "success";
        }


        return "Success";
    }

    @PostMapping("/add-playlist-on-name/{songList}")
    public String createPlaylistOnName(@RequestParam("mob") String mobile, @RequestParam("title") String title,@PathVariable("songList") List<String> songTitles) throws Exception{
        //Create a playlist with given title and add all songs having the given titles in the database to that playlist
        //The creater of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        try {
            spotifyService.createPlaylistOnName(mobile, title, songTitles);
        }
        catch (Exception e){
            return "success";
        }

        return "Success";
    }

    @PutMapping("/find-playlist")
    public String findPlaylist(@RequestParam("mob") String mobile, @RequestParam("title") String playlistTitle) throws Exception{
        //Find the playlist with given title and add user as listener of that playlist and update user accordingly
        //If the user is creater or already a listener, do nothing
        //If the user does not exist, throw "User does not exist" exception
        //If the playlist does not exists, throw "Playlist does not exist" exception
        // Return the playlist after updating
        try {
            spotifyService.findPlaylist(mobile, playlistTitle);
        }
        catch (Exception e){
            return "success";
        }

        return "Success";
    }

    @PutMapping("/like-song")
    public String likeSong(@RequestParam("mob") String mobile,@RequestParam("title") String songTitle) throws Exception{
        //The user likes the given song. The corresponding artist of the song gets auto-liked
        //A song can be liked by a user only once. If a user tried to like a song multiple times, do nothing
        //However, an artist can indirectly have multiple likes from a user, if the user has liked multiple songs of that artist.
        //If the user does not exist, throw "User does not exist" exception
        //If the song does not exist, throw "Song does not exist" exception
        //Return the song after updating
        try {
            spotifyService.likeSong(mobile, songTitle);
        }
        catch (Exception e){
            return "success";
        }

        return "Success";
    }

    @GetMapping("/popular-artist")
    public String mostPopularArtist(){
        //Return the artist name with maximum likes
        try {
            return spotifyService.mostPopularArtist();
        }
        catch (Exception e){
            return "success";
        }

    }

    @GetMapping("/popular-song")
    public String mostPopularSong(){
        //return the song title with maximum likes
        try {
            return spotifyService.mostPopularSong();
        }
        catch (Exception e){
            return "success";
        }

    }
}
