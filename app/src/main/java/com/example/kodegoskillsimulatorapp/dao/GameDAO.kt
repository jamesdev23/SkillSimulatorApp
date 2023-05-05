package com.example.kodegoskillsimulatorapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.kodegoskillsimulatorapp.model.Game
import com.example.kodegoskillsimulatorapp.model.JobClass

interface GameDAO {
    fun addGame(game: Game)
    fun getGames(): ArrayList<Game>
    fun getGameByName(gameName: String): Game
    fun getGameId(game: Game): Int
    fun updateGame(gameId: Int, game: Game)
    fun deleteGame(gameId: Int)
    fun getIcon(cursor: Cursor, game: Game, columnIndex: Int)
}

class GameDAOSQLImpl(var context: Context): GameDAO {

    private val defaultGameIcon = "iVBORw0KGgoAAAANSUhEUgAAAHIAAAB0CAIAAADW5vE3AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAADwjSURBVHhe7X1p0CXXWd7Zerv3futsmkWSNbJkSd5ly8Y2xiGxCQYbqlIBKkUKQ/4kRaWoyq/kT4CiWJxAQrE6mCoSAqaSSiiMAYNjjB3Flmztmy3JsrZZv5lvv1tvZ8nznL7fp5Es23MnTEpU+Z0zfbv7dp/znue86+nT95MhBPFKoq/nRuL//tluh6dmZGU727s6ZGwy2wOh3b3iZqdIquPoEt5ecbDuswteQS+w+/Vsxg6EqwyrFJfA+uJB7Y583HY0wxfbVxqsNm7B3ItE4BtgSgqX9utvn6o4vGitY0lf0nIkBdbAwT7Eadx55cKKDrwU0xd3aEZXmf1acdi6lruR3hfJPXoB2Vc0rDPWO772ueu6Iq82jC8lSVQ9W9/DEgy8CNYwk+eOsRmbr0DbOhMH8LXPGpkOQrKPrbdGGZwLwSupOlt8aR9e1OdvRZe08PKUVvE7b0WqgrcyMbjFB69lRBPUwYqzM0Tj5pUG64y9jqluGxkNUtTeKsVe4UQIDuKslaatuKpUx/YAXRztID0HmEdRPrvdjiKf3fYVDOs+XziOAU0drJJa8jioEBLICy+7upFAHZJu6FqKpUTbWviZ3ad9AKkOcXCIc9SjvyuwQlTBdOQbu/RmBp+4oHUhu7r8D4Xp0OvkFU3DKUnXaux2LWN0lXCSXgEc9rpzfyeMADCs0BOdANYZpugBuutDk/Pyy6dOhS+fYNGhDmgUt0FgMyERxxq0jdZnNRFWuwdrP556xbusCBpgbILTUjtnM/grXNR6KiUu2LNsl0sv6e7+oHwjGJq4BZaANhEebcZblPAzJiMHONnh/Ao1Al2AhS5c2s+Oae7YGAbgojaIcbXx+BMHJ9fEb3BbNwJ7t11Ov7pbvjnVDtohEusyq996k8iF06HRxLbjUwvFWLBrDVuy/ncHVhvDKY2Tnah65b723H/7/T94z2PLuEBeAlDXI++7gXgp7V956S3fhLJ6XBdq1Nejw/nbfuZfikUBZJF6QYgx1hDiROxx1YlrxrvmhxU3z/gBAjTVrWAiAuFPoSE8R1QcDbr1ouVJqlDU1f2mYC6hRdyDVwVHPBWic6ByxUgQUHZGDZ4XNwaJpjS4dZKYm6kQD+4+9QsfXZocREey1iV1q4FUqirtK+u0NpoGUKEu8JcEVBtQZyVwFbwMQgpyg3PKxaipdTrPRSJb27TCG8PbXdvaVPemCdTkwm3Jkf/0E/WShXgaS3vaKDDvNe2CYnckztWl0D3UyR7NRZeOcdzHhqxzEw/iSRzE88oq26gWHDTAPMLNAtcZQY6lu4eqxGGfkzIIiwu4ETEs62mtbiFPOse2Vf1W9BqVN0LXSrVSt7LfyF4j81YmLTQbooBRAs5SFzmGzDcWu0YaD4VwIk2KWTOXJ9p7F+2nCvMS8p14L4BDbzoQZ/jEgg1O4itwB7GFMETphjhSev8WqWh9UjsDC9gZ4Dao1udBJ16qICXGEE4aUoUIIkDosI1yBRPRjSqI0gumQuud9VQd6rUNoW5FyXrjRZ2HwkHYPwP6ejGgeMXq/l+It6M56MWsInyg4NRMCFUqkiyYzKsUpt8J7RD0CegX3ECULlyNXZgQpi1XQGlts9ZLF4eP9ZERYOYkpL/jA4MshZEeCp7okDgWgwuQtNlWOhtLC/iN1CBOAKgkG5h8IGQ6g591dPy9PJf7Z2OPrgzWWEdsjbvAFDrY1UujgsLcveMHWlkImwmbCAsPDuWP+o/vcUvgtfFSXsvPOLExH0GKaG1jsIWea62g7g5Rrm+NZz6kfdC+MaKKpU5CkwR86+HNo7XGkKIkUhp0wllb1aKsRF2LFjY2+gr0LcbO2Eb+ojJ2Hf46wmkwMj+sETLc2cFBInw8QMNAzFHZI3JgOJgmES76S5auB1E0X4Yz1kMdnI9UHFKaIngoB9ZYq6SHsso7BWYseEYo0RU4H3yDrfZWu5byigJT6lprkf+HJE9FlsLNBQh4HztdyrrX2W9F4AM0P6yz+nHj3r04ATTC/ic2XUBAoIfM/8REihquU3OLQqmNyeeLqBurOckSVjh82BQaFe9dUNIMskkWRoVFmRS2ypoys3VqbYKtK1PXZL7K2iZ1Nvdt7src7Wb1dCAqFFwTmlK6SrsK5lkreEMUGoF9nl8iEJdQ53WvIG6F82Ew1MkV0ziABDgAFuN0eKQ6xXHInDS4FPEdpDMVSKJxEUwrjsgVXAkjdgk2OLnHM2Ak2KAQEnDAaD/i4H/zAOu67QO6cbCKwujgWxvg7DN/tJ+++QaXIkKAzY3MscooBy88TdhLQNE2vmG1RqwPy6fO12s7mQeQusW9hRnEAOvsLfr4b31ovICOqALGBYIiGWAZ+kDWT/5ELXyGE1cAKzSFcWgUzA5WcMh6WwMQ0WqV4yD0p1JMhDgIhuF2fUMFRz4NH4ZuMFZHhm0RAgqVdflhhyC7+XKwRuPD87gHRwlgfWDnqV/83RObBwoqtxepsQivjBtn7uAPvEP8yJvFIN6B++Iwxnb3cETBDkr3bXdYCjEW53/hD/WzO4uVwqDgi0muFiawZvbMrebEb//4t4bV6mBMHMB5CK3vf0Qm48w5DmPcAT2h74AmMvAWu9ECjJVqTN4k/alJh0psQoShVrSGuJGQgQncu1flfESMoZ5794PQPZEGYGqXxHRBNEuiWvS7fTHsCaj5ZEGMBgKHuwMxWhCTJTFaFMMFMUX6hHytL8Ypw2CIOQRH2eAcOsaqY/bQdRANdPuR9trdo9DUsHNXRhGLPXrhmQRO0p8qXQfx8HnxiUfK33zS/cZjw488sfXbj1z8T49t/c5j4489uvXgENbWCI1rgSOEjcJE6zC36iAkjbdgA5ZiGJcyJiVLUAI7C5lb6BLMDqwTWkFb4BONdlvQzBvgmP4JoVmslYUpAlUInjAOXnc9v/pGFNo0yy4B5/II3qGbOupGj8LWNQVV4KRbjPYRkgvx0LNn/8fn7/3lLz3/y/c8/6v3nP2VLz73K1945tfv+tp/vveZL627HcKZxHupbBRdSu7M110+zbiIRG30IqNRohYBwYxTeTYXfkH4PuKS1mdO9LxYEGIxiL7zPecXgoekcp4UNzqROY+xYT5O048cd4YkzGW38y2IwUwEYW7iMLLgZjbVhTgQNxqXGD96AwncEmZNZrvq6LY6vmmObmXXbebXX9RH19xylSPD5J1kneEOD/bRmYsYM/FGiifVH2eiy46MxPMg5OxkNp7FBoX8QwzhGFgQxXI+AoA4kThlAAtFh9ezNtzATlFqulpind3H15FWfDg0O7hsmkFItvgZvekLRoCwMjXkl3XSr/pLSuTKJc1UOJfArXo1gMS0DS9n4EBM4crowgKs1vzpiWK7rAEChgAOqg6FpwGk+0DUigNESaoUeop0LlHocU1vw7yEOtJZhAgj92Mv6DNRYpTCcdjrbNxSEGaHl9ALJ3C5guu4MtpvqaPIDRpkdfyKOjSRehv6LXd1OnFi16uplbXTrcoQWMYr0fGAeIAChZugeYExxnwUZAwNEPkjrWJhvCc0GcL/GGoCeeyyQbTUIRn5xUcUXsiXAO6z72olWQNFnn6Vk1uRom+MO/Gwo1k9l1IchUuvuWwicLHMNi8mmnbaIW9S2R94v27Tie9XoW9FUgY1bU0NgYLCRSmD3O4bAdDc/ATKKTsNLFAog6CoQhiiAt+1iNRUESdCYXpky/lHRAoa8kgd4Q1gBkbJRVitkq1EWgbEFScWKJ3kL1oDgBaRfJluk3jagYX5pZX+WpchmzAlZTVZLbJGt7UWpRQjdAN1JkPOgfqyKt1SdX2oToRwvSgHvanpt6kp5WIjDuHWpif08Q29uiUZbRkPAMopNBecS0THCMbGMUyOnYe9Q6zjahPqBBJE552mtgg+R/Ip8rRWTqVQA2SlyOVbAAQBLMEPzkFgO01CUJ2IJsaYxAcqjrMB6HvgTu/P8Yap5ZcYpJFBDsz2QV2Aj/80g7hr3zZEnFlZ3NamRUw5N6y03DSfjOxMAHaQAp+KJhMtoORSGdhKH7BjvIVQODMUZiR05UxtTe1VwzCBpg9XIiep0R94YYgskYIj5o3YSaTIUKIzj0zSXGrEZE4qWs84DrXx8Pu83iq4+LQNCXwOZZD2HZfwTvYepyJJigXkgQjiHEMxbiHx5LxjwDIYoFC3ohcd61yUCY2q5od1NmUSH+eyA5FpQBmQu3ZgEXN8apFAGAKsFm1XA/OH8Yftg6R0t3KKkBlvHG+c4t0Jk2JWC6eRwdSyenQ2TpPA+DqhbRyCiJl3Coobp5UcJ/mVZ3lBvrpW0EUUGWdSow7zgr1ruktmn/EGjArHCYh7+NbZl/OQMcjAZ/uXTbAcGM5GFE7xSUOEBB/oHdebkA2VCZVDARuZeZnFuBoYYQhxiDwza1TCORdcyVyL8RHidpgUuOkaCUJXJ8MbAsEOstkZ4QZoIA0Rv/OJo4/q4l4rA9GPEzqM9TCE0YIEgJRwp2EGTV2LgRMK5BmF875gkEY5lkbH+KhjA6aVHSR109jfmlxaGGjWnAS2ukCR/epaYqaEL7iN5xmaUDS6ziJvdoYP0zzUGeClisaDt8cbOyg6QYkCRQQwKJzzAvhIutnx+JwLWzQYp3c7CYVkKRhK4Ahc4GSoNZIh1AyUWC3qZJLEIx7uo4STMb4D3AFwUwNwF5o0oqTEotVvEX++PM4qRdXf9L6XIygpkpZEtEgEMdgRD5pIFBOQz0Ar4W4BIXKYyWKzDUsaI/YuRG0TX2ehhEWOVhi+pe6Jqi9K48bajXthItwk1gZRaw1sBu0vLusan8GEGtF/YVRpEFsq7RUGCFY1s5LPICLiKOChM1jSQ4M6O42xgSUF/hQJ1AGKYMb9S1rpMMPofWsh7UZsj2gC7SwemYcIJWduZoeolLags6vYiQVd5VOmaqEZIaJBxBLj0zoNZeEnOb15xzo1n4XijC7QDcd1IX0hB/Bk8IixCaAQw9s4TW4Jc3xMY9TUKLosnlWpFamjmiBNoiWGw4k4slqI+Syi4v4LheAxQIUGUIPieOBKIhkPKTeXTd21MDSpnj/AgosZSbMhsg2htrXYNmJXF0O9ONT9dakuCHlRJNsq2YQam37D4EZrrxn8QLGYayOs1lMl1oXYVotbehkJ7kWdbCf9YTLYNv01k60lyVltTgtzQSRTWQidxL6z9Yh9HNQ4urDu++F6hIlXNF2miuGdDR72mGjR6TG2VYE2HYczT4p0Ik50RGB4C81ItL8YyPiwIZZ9I/vNiYg2cJsxHLt8+uSaeOL8uhsgLoLGczUU5Cw4axD6KDEFU8ENlB7V4gtr0ztPbeTO+KTfMrxGEBbKslwZFG991cE7TsjcRa8SIYCw4JP9ykQ5YShVFELurH/gtkM3K8rtjIgil2CKaSLu3/3qh3//5HrP6AQhsjCQudAqu5HVx3/ou8Q/vV0sMURlvEVpnkkTbu1ijyj4s0Muj8PBVIhKPP7Tv7/y3HRlB7lDMHkxlPViCYvlL7wuPfKbP1YtoiaVoqvoD4wLkh5qwGydAD0x4kw4l3lh/Sd/tfbw6acnedK4xLWQo8QjwYO1Aptp6hM4jjp14DHfND2r06Lxzudt65GO93umqa21biHzqd2GmavloBIDL6EDJUwAxAPShBQHlIXJIbv+bz7wrvceNaswlDAr5BSXICkIYpqK+7e/9uE/uG49T00qrMVQIC4IqRz2wuoH3yZ++E2cp+ruQgGQ2Mc2iiS3GEacAXUAo2CQR+LZX/yv2de2D5RJBsCTbKjqRQyhcGu3Jdf81odegJVq8TKwtjA8tDlzwvqBvxnef+rpYZpUcOg+D/A9BpYRWskBjNaoFU1DXvO+GCyI8bbw9GQqVKm2wMsjJfQ1oilYBRsW21BAO7UcI2wAf7UqjC44TtX6MX/hF3/oXe8/IFYISIeEjQlWkNNcPLD92Ic/9rpzOWfEkYBBwttapGqa2t5bbhT/4HZRRP2GQnWwctoEVgDZSrQhwBT1dUATWdiZVEzc6E8+k5wd5uN4o1TjRT0oYf3bM7foE7/9E5MlOFKkHgyXv8GqFtrzuWH94Ken955+eohkEaY55AHBKbB0zihtIaSI1o2C3snGInARKTgrReiDE2mnoRmlJiky6HTbNiP4HU51EtZaS0RUDnzWMg8hCTJJ/ei4O/PT33/H+4/qVQcbSNeCqAiyClhNDVh3H/jwx24/l0WoYbYNxR3D5Sb+YO+inFQJBlCmIdA8YbQ1VSGHlsUnilYFxFKQPUQOMPqcWlAm15nbnh6Ct2sAC6yKHC2E2UOX11wCK1cMvTysca3MFUzEhV5wRXC58plyCWw+o/ma9t+gFRhcq12tpDM5cc+ERFqLkdVS9mUyELponBpVbSlVxTk6BlOV9KX0E+nHnOOYNn7ShrJVrVMYc8T7bJVCFbNxem2cAbbIVrGVwUprNRdYMK0zKoWpHdXLtT4yMcfG5sQwPTE0J0bpsd306DA9PiquGRWHh9mxYR6/So+N0uPj9OgkWRpJuVX2EYwxK4HyMReYPXTBbnSb/PdNKY0zNnPDipTGq75FliXg2wGWNspkJkkwhtiFlgWudwhIPeGoW1qiOIdkpC+C71uXWwsBSkXSEwYlF7CMSe6zQciXfL6EWEv0M5GnDSdJYXQZ4FJwEIehqm6WjzqL4nPrbfDeqJCZYGB0AX0IORhJUoxra3qNMcgWmkQ0Jm2NQbDH58Ox8JAns0YjvdON6cu8n/Qwcg3svwo+SypoPwI7GI1LaB/Zl2A30/q6uhJY4etLoCXbQIEvLY5EKXXtwrixu96NGbTkSuYqQMVlreww0S5DhAMT4RxDZbijpKDbcQHGNbFBwmFzojQTrucboAYEEQf0nRogGGJqi7oYEs1mBtgvxEXSIdeA/qZCwWrgCBYNgFDGPFJpPjpVSOJY0JbCYMMAVwZaC4mHs4Vqp9LDMaBpLSr0w6dOJ4wI0QYfnvNBVkedqfwWkhoJrvkKYIU0AQkakMzJBJHfhEWMtS6VmgpTCsPss/ZjG3DYcnZNT7SupIRIWAPHaVtXjbkEwtZFYwe1X2g4qaGRqTY61wvSdc+fejL0WowFGo0JPo0NS0xPEZEZW8Mu8osgGgcLq0TKZVQApXUAeoYpkWE3YUJga600sLD0XXGRFh8E0J/DevREbVEMHIRMlQ269VmMSRg1dzaIldAiYOcbkuQ7InPDutLsXl/Wx9cm147apck0a9pFH5am9UFrlyp7uGqvH2/etPm1G5pd3x4UxUnvrzHZkboJzsFWinYZfrXtZeHa8cbJ8anF8FyTn62L89qcSZKndPbYwnD3qN9Yqh44Jh46Gp4eYPhgsFqRNhhHzjsAWVpwBD0Ih5eMzyVSaaAIT9SmLdWdczBJLXWrTZtmNoOFSUOSaKkWrMpdK6FqdK2OazMxLBAvzm+2YpB74Gm8NbXNa5tOQzrhIgS4d+X60M8GtgcCpemZOV/R0n8y2PAwQPR78CVBL9h67kjgv18QaxtVKxOV6yltp8mg1RM7yMwU5l0jMigLV47U0h8/7+/ZybJyDaG9q3yaQapLb3Ta1t934/F3H5Z9W5Y6G8PPQHUAmLStVgs1DJ2YeNvrybzc+s4bDt2SCNhyKCCMNgL2JviVgNxOiMe27/rwb7zz4rGOsU5D9zuzbw8hkzPZid+1EuzSVPNypsXYQv5gUWlZmNpwAoj5Fe0NcsV+urBbwyScP5ke/Z1/PkWsx7QaKXRw2sPpc+4OLYSoG1yhUCNOmRvWYYyawShuQ2dRESw0bCFkCofQU7icQvgtoX79keF/eWRzPeSyTRLIj/F1gCG2B137r/7+jT96UqwyYaL0dXkO6oRkoF7O6jNYYRMDThAExQkmD+WFQUXjOUwL+Pjy5pd+6/duO9sHAB1FqGZE5Y2YghhHdPvSTzUf4tOxxjk/JriB0w3wvTiOF/KyeCNO+PNFfWDkUy3WXlXc9B//RXWQ3+cIfpHvwWjh2jilEIWW9qbXli0SpHlhbcNWItHTjJMKEH1GOzD5XlhEbBg2uJUpmqj00r+/5/RH7l9bWz4EK6k5n1LBw6RJWB4P/+373vCj14slqBUpmj+uJkJkEa0e+squd3zGDZiEr8ZWGlhBmD8+jtmo1r9wz6HdLlR4WYq3g/b7KH1jClSPJiGwPNNNKcDCchTQahwZnJvdEeqezqZcseMOaf2P3jwpKEY5dAHcYnDiXWQwbkEpa1RzwwqrLpFJog6UGEOTA1aMIY/MIdCTdpxkv3Lf1u88urU2gK0bCJmLBt4sIC3obV/86ffe8ePXiUOUVNwJe4QgBjqA2lIREEskBAIOJMnZYbAP0WNu271qigAVZg6ym4gKaUKEdb8TL98bKvVsFzkhrukuQ7szipzPTu7t7H8Jlwyt7AuxJHYVVZNzFC9YGZS921GUqFpoxpywlowFIDm4nWIKFwqZy2KlHHh0nxZJbBnx4Xunv/PY1rA/QtgkZQ/xJS298sVo8+c+8KYfPSKOcJkW9Ih3J0wqAXMCjw75j0vg4jwhCvjHKVwUhQmNNKGFlu5BRf6jzYj/44Xx2u4Il3Qd7FCgiuGD/8l7Rx1C3RHTgE6vUSdOoRUDVYd+GDHRAXkHYM0hAF19oD3G2IITU9tmRXQXcxF0B31kg5E12iPO5TAuZCq3dz5B1hMXRudNL2mKpMpS1+fK7DrNRB9azOAecSWyVJp8DDfyJD65AvtxJlp1a9bBMbIWNIEElRSxBOI4BzNd0RaAHUZb8ekfTUTMtzgjFgt2UC2yXjhFFsOZLj5r1JHH7i7sR2xmaMRB4mSiDEi9uXQLoQdqSiOmMAJgd3Zt5BAuAch3FRQFLpl9OQfFR3xRAlBgLhlkIP1vGOt3wx4XIQEFBNPYQcCScGFLXOfuUiWLDEHOGNBEEeBCHWS7XIWK7AF+b7EVi07k8f2Cto0JUXwEjbCdDaMBWEYvUysHwhxCTCRUwSKKIPLuSTCK29uZFZUFlbOY3JnCJTmKxTZFKXyeh6wv0l4shUgzrxM+veHDSERgXGoKp9lUGB/UBsjYzQgoCgOw6MZh7Lp5b/R7blhxC+UG96FeePdoXzG8sOGdvsZ5T55i9A6RymTNhfg1H9VxPXFjQwOrGCne3YlhHCcKArCOZhb1G/jF2Td7BVfgkwY3Jd6IMaKsSmbJqAzhO/ma0f5tIFTUmZSuMLKIO/tbpsXx3u56UKwooW1D15zJsAvbzq6z1tnVvLujfSgjAHNSxwZrRIlAsGPcMnyjoYagej7s4zPuAFs21qnVGcZ8ItxI+KkPNWAh86wEIxBtQLQErFkj34qFM4dcE8/XKOK2e0oWEL3QDO0NLRCSVoMJ2fKRC0J9pLu8nTF/nJ/gKHCLu2CmEKtR7/dgwLa7gA96kV8jL68d7/Wzy9gRxnf7OIIAwj51CNBI4Cx4IeTzwxrz8i6KRiG3SDMzH0xr02ChiZ1NQ2Op8RmSH1vhFh1aBYSpTc4AhY4/mqjI+B67xBrWAf5LcWYLMTdu4UIV1sA8tIsYUFEVJ3DwBdGixeE7KtYhjUWsVzvRtKKyTGmbuChw5raxhQUi4tJ6ziXGptk6WsaY0iqhxEpo3IB1ZI3jvQ8lmewIe1E5wDIfNaPMvtt3hpdNGG9+kJsIblcdmEGvOaYo6AVYFq4uZdsEOfA+9w7OMclNkkoNZ0R7wUqgUfAK3T43QKkRCeIBZqrMMBINngEcog9kvvHJY0e4HErLQ1oMWG04yMJo3IIeYWChNpAboIkdWu6oyCQrKhgiqYJCiI9awDPjb3gntEWH1pXo0/gWQ8pVKhKFms3exabBQESw6/2M6He7SHZ+WOOyMDhJ1I1aEAjt24DoLNGYJmP83mQG7ipZavkuZCZVAsA5fQkG4Pj4wQJY6ItULSRyKxzxNPBCNzQ9dbfAIGPXqAOokdKBrIwaD1AogXGFAYfVxReq6rGY7ojtkRiWYlKKshF1Q+FtGwgphx25J4NgwsRRiNigHrhFJvUoaNQQHJxsudyV6SmvIbK4fDa6MVuleGMf3xjo0AzPK0gHIAK8Gf/RKGFEXzs5VVw0gdwQEr0p1L+7Z/ujD64P+31EKEkwmWpaNxSyPiraf/2+N//wCbFKdePjT1SVRncKoFTgDx0AWRLq3OcOF82sD7vADlEtUDdsGuwLA6GN9vzF0drpi89tDC/AibbOutarYBb7y4cOHDl2+MRKsdoXeSoKHVUh8XkKjSGgs/r3Gt5rF1s0BLHBebbI3sIQU1RxqJkaoesQKERwMI4jRDLct3PDykf2CNpjWI4aY+UMj0FQU8T3lN5Qbsni1+6++Idf2d3SEJGkp5JUNJUdZrk8FOw/u+PWD9221OdMhcEtEVZUAVUF+/TI5L0bNBDPzWxM7D6NAnpH22vdTjq6OD1/bvvMTrm5MT6XH0jObj6nMgjnlC+9Ihzm+oxEBdifVClzLLvpxJFrX7V4UyGWjCigAQZfxTZj9V2rOIzJWCQ0jW4yIoQpifuIBckNoiuqHaK3iEXC+R8IBRc9zA2rRQSOYJHvzTQxNYKqMhCybTAaosdXIF011at/dlF8ep2PB+F9EFdiyCtXFYM8H49/8OTgO/pw14S1gRmDRqM76Auwk5POqM3SAPAOIOOkxh6scCPIPOC1a9/aZ+x9Z9ZOT9rdnXZzp7pw5OSBx599pFhOW4nxIkCK64ml9oz/tdSLo5sAxjVLJ2694U3X9m+IkgtbBmuDutFCh0aElQLTrSXgWYg3TqG/oBSwQrpwFdJsfLYAmPtT2L8oIvPDOiehuSgJMEtc0gMiYhEfrqkCm2ifqwp9kH2wysdTCi7YIUTqFmezf7icBZIBEMRU1lOxcVo88jcP/Onh6w+fO3P2ttfc8sTjj+cpTLxv2wrBTnw7GNAHrxBTS+chkEaapFemSDYqP4aBf9WR2+5Y+e6j4mRfLBUO44/W4DIBY2utNmqR2tf9ps2cBHG4ugT5g3ztYxoxRmgEo7Q3n4cPeAlKwz7Rycf5lW5I9j6Yc0LyJrXYeej8PWvD873l5efOPFWHaWmbxZWDiweO9xaOl7Y3sfnEGmRSIk3aYC3MUK5hhlo4s0xOq0mSJEWenjn99JeevvOce8YDPwwdWlFpO4UkpMZkVdVGzb8SuurSKrgsaMDVzh2skD2xE5scMKzH3h7c0HSHsB/RErMDqhvMJ75GFNYFNwxLTb0Vzn7mwb/c8Bc2603VS0S26R0sT1FXor+w3OsvbGxvIAMNSdv6SeNLiKxSis9R2IBGdJdohNGNlsrIrBmGo/2T77r5fa9OX5v7BdpOsGQ4ZWlbn8C8zQb2sin27apLawzeO4vUUYcUClOmKCBRGCP3dEvAFJzF/C0+vYMjZPjE6QzjpmLnkbXPn6kfrosNvSR9ZrzOkt4g5FovqKkbblfna7VZis1hfWHSbiPwSHtJnvERJeqGQ/B5U/kJMtJMG4TQuo+Lzt79tc88Uz7RwIpGfqoKtlOrBFlafMQzF7FT88et8xNX5fMTTfETH/CWUHngi1iVC94hRug1esS4J442CV3rgusoASNf1qK889SnHz7zeb8yqpLd0HdWOyvNuK3qAOs8UQaueGN5AbrbIOtbMoOBXA5jXe/CIhiYASO91VPDh6vaNs761mdNle1uhDP3PH3nRjjHd+WDSFPG5miXaeC8BLZhz2cHV5HiowQQZ4Ti8wDG20AWTcNFdbMHkF5YNOxxrVyUXGAaF18GLqzABXA8z7snnrz4QN0fNdnU9+242fGmkhmkvkwBm7CJdWYUFspBf3f1huRNx8MbrwmvPSxuXg0n8qYwdTAIV9oyT/mwumm95ESVm7qJ79dny6cfOn0XrHYrm735GrY7N0X+/z/YViQQ0YZqH8TUi1x55mBRWUpIBt9JiasENLBuObmJY5qI0DBCxRhAK6VfF8/98Zc+Wi2dH8k1VUik+goD5jPnJogoegjyq2RBLl3Tv/41x998JDuZiBUkhHGWZLIlnn9u/dHTF5/YHa8312xOd0OWrKZJf1zuqsyZQtfjph8Wi3L1va/7gVdnt2dikdwpUYcqZ/Y4F1E+rjqsTkyU71MWoel6wqdCYkCvi08GVcAPcNf40oQB7HCdEVaDrAmi6hgsAtupmNy3+ZkvPf8XZf8idH9clxLI6IwTr8KoRqdN79qFk2+49o4bklsSsZiGRQ/rAgGOAWUjJq0YrfvTG9tr9659zKm8QhrivQ1VguACVkh639jUZsf0jR987YdWxY0abGHwDeJ9OtnLp+hmowW5qtRygjVaR2SmXOrWtJBZcIwzjCU7BhhRRWPK0y3+4TxfN+Dr3py5EdMnn38YdhDRaGO9YQSQcNFK2/baZT0a3Ljyxned/P6Tye09cU1qF8WEE8CoBll1TllGBn3NteqONx744LtPfk8yWZSuQOMwtolMyp0y5eKl4HrVxeb0menTiPUx6gzxIkZzUcu5hr2pgatJCwbih+KIDwLYIKqZAQXP1GVazlncyje7IbkgHMagkZHW+Jndhxs5DApBPdBPJNfzGG1V5jO9Ld943e1vu+47V8VRA+WFjUW1kDBUyZeN4MprnAK4xNeLW4u3v+u279V2IKVGloDcdpAW1aiC1jpjbVqd2zntRcP8jnWAn/kIsZy9kp8Xm5tgSTeFuX968ZMi7MaptowLf2lfSTpGpuwCjJlqJNcPx2lwWgl8V1sxfPLUg41gmK4kzYJy0sDf2CTz/aPp8VuO3LoiVpOYIeOm7sZYIHL0kww5YLvjM5G+uP7m3ltuOHhL4nLf1K6ZpFobk/LFLmah9uzGmaEYRtXHjXPDSlGJE7lXlyh/yAiaPxtt/5GYPCQaPg/n1FMH62z6cg9jBO583jgLXfFpZTkVm+ujU62qOH8oE5yiPeTL1iYVvTtu/IdH5WtSsQzQIOYMdCGccHUonLTEMGCLbhI0ziS2/VSsvvHY21flioGu+8qqOi1gnRKPBpTcLbfP7ZyB73zx5PXlEnjUL/zA81WjlIyt12v/qy+/UJ35U6G30Hk+bI9wRr5pYQkjUeOjU6KGZAceiwbCXmjOuaQJiUUQhhwpLkYBgpwnypL8usU7UnEE6p9RWiPFB8CdpMXXYIDs3iwqwNbSt/qouPbGxZsXxSDL0sbUU18x57J8/hN0+/z6M7Dv9AU0XvMRu7XnMa4mJRMx/ZoYXizsqVD+bzH+ghBDMgsgO/X0kFA6GMgGeqJDj6jAoc7mHsPGcEtkOmik9hVuiU8kU66hlLo3KKDlSIVwP8Iszx8E42JkL6YAFiakY+GFYAeWWtnUqFz0b119Q9EMgpQ2g92BMZWab8x5nbsLo9NTMXX0ontDddlE2egE5uqS+sz49MPpuK+nojBf3b3wZ0Ks7fUTySKAiJNrM1iR+ceZJEQGCcQN4Sn+Mxl0CuF7JXQrJTQ34epDqfoLBRQdtoCzMkEkBvoHU65SZyB3GDycBnGEYi6H7tZijIvTShzRJwq7aNGGcq7gs59ESr4/r5tSjEs2DTsy91yLd3wGfNVh9f6LO+un622u7RXWTqYPhfYcg1YUfsTgOU76xxKnh7EHjBndQDZhTrPGB6RDDm4dOQXPa7gFyFeBQEtuCbkj5Eggf+WTPSNajFMKE8sJbPYQNaIePsdFDU7UFhkvhq1MFtJlqdMqMG5D6AqTKIP1oZapa0RLzsDJnGQ0p26vANYx7B08a4NdtNrZMC7za8bxzSYiwfV8p+PCIqGe+cSR4s589WGufZuKw2U7fuqjRn3Gq51KDhq1SvOloIhtxrgEORW9GWBBbQ1XYKrCtFk16o/NcnWsVx/lYmd5QWUXMgSf4Ugleo6X9aG9ATKKDnUiCt7ACcwKQjGfaIRisZRiADOKr0w2Lc1O3asbBGWuZ9qBtVyYUXtbBLcoWmpNV9U8BDCcu5K4NePkadyj8+i6wZcldRcs0gsGJHx9EYbl9leaaenCUCRx7lL3ECpW5fPNziMK2Rdklc+s4xSXRzQqGdXwyR/XQcJ/Ry2W02mFCAhuzMFC4AZGWHxnwXo/aapc5PAyuD4uyooJAMJPSC4XHO2x1/GJAeO7KbHPfD5Uwc5Wk2opW9aOv5SLQ4fM2hiHavai5nmJD3g5jzkvof+cAEUWj4GJrKNtvlsOWBE8lRARRNV8XNo+O974I49EUzZkkqhn9Dn1U9vnPi/DegrseDss7Ez3o35DQKHzDEFxEywnrlBZ5jWdE2DFtVoiHuK6zJ3pkGtBG6W5JiWu9ueaJcfoS3hcauGjusJoElmyK+D+wHDmR8nObrObSmNqxVc1gne65qoLqY1Ps/hUgHIzJzEVjoM4JwE2dIzZNqWMa5e7OrhYBNhMYR8pDwBm515Z/mkSJnApiCUR5bcQCQn52pblY2LrARG4Ens2WwReOrXlJAdtLN+f4C8SJgeXj1pEWLDSNKwIu4AA4tvEKr893cGVUT9YlExtSOIjd2wprtEMsPAhPM8kPYT9LlRi/NXNJ6dq2ksHYdfnDoly40zTyLZt/OrgGi1gk8jOvAQpwF3zw4oOROriH/SSsHSj6hGGQ3E7H7xWrd+5LB/TMio7DCWF1iKEGSRiINZ2z31KTO5ncASzCNHm8h5UzuUC6E78bYCIjciPZCerqWyRBvEtOPYUzjYAPoTuzfaF4bM+qRh+xTUXXLjMfIHOCvFrp1UxkZvxiE63Yborth4+/xDCJ9/CkmYphivUIUF+HYJNTxw8aWDlPaQbGjknOTqX+WEFdxQN6iaOopTsCayH2R1AkaW4KIZfaCcPxp8wdKGFQYVV7SHS5jSWhIKN/PRuN7wT9heJj+XDfMDKSfu4WlAR5QiDdLIQh/vpEdyndKs0gwHnFW6BMDd+5/6znzwvHi3FeqsnUHmMCawH7qawBzRX75U976xqaeqHN+6fmGEpplzcDnGHKWEQjexPL6Wrx1ZviL+IgqGZX1756OgKYO3uYMaSxfWhUMvoK5h9p3w9LSBBfGS0/uepPEUphSuAUy17aUhTKiVsD3xsWEhOt6O7RPV8xznUk6IBMLpQABXyvSQ6LmRQt1x/eyEXMWBelDJxUtN0woEJWT45ve/LG1/cEkg3hy28UFR49gwoIdKynaSiDZziN5W8+Fzz5OPnHg09tOEkf7A9RsTGKifTNj+6dMOSOgycmepdAayBIdLcsHJyZoYsomUMKQ4gYgj9wHPUdj8R9X3l7mczCCCagMRCK9s29w6Ohk4lZsw8vfuImD7EDtOUJBRSxVfwEKDyJH+AiJBAL95w4G39sKStdq6GUQ1JawM8FhyNnw78oxef+MrkK1MxtrDsexMABISzrYVwfDwB5tr43tWp8PhdX/3sjtvYmWz3+/3GlT5tW7gtJZM2zeul4/1XZbBLHIU9uzEXWWS988OKsUWvZy6S3gU1ePjomDDF+vyp4TOfWsm34yq9pXGJANlnRSXdEJ6cj0LhfiJug6zcfOrjQj6awDTQlAwQ6iIHTTlnAjBRG00BgEKu+eoDry3aZf6gGrCV46yAKfUwoy7v7zT1Q88+du/mXRviNKyBNfVsggXVgB94UgQootwUZ58bP/Kpxz9x3j0bMucd3xRDX9rEeoypkz23tOqO3Lhyq4YhidbtSiIlFc36z/7sz86OL49iKIRxVHF2hAMq+dolgsYcLOgwFhc+4Xc+meldRH+tXbJGZ+mYi0dpqTibD4fOMBNjaql6Ju3LhTc4nyvEjnKsAqwzqiWs8T0/LoTHNuunjz/z2GA1ndqd2k4ybXqqQEJUS428xtpya/vMhe1np3JD9xxUodYt8qxK21KOAfez40e/fObux059cb3/TA0kEbHFlzlV/FX3sq4LsZgNF95z8/cezV6dhpyPs9AfqiD4nod806ps7ocu0KP4yZddCA1z7RqnvMgKePvqvuaJX1PTvzTZqGUceNCn9UDtcMENuIQ5RCQDOaoE/3qG4x9qm/Tes/Sanw/hnfQpcqj9IoaKZoB3lIxkY8AEcXtg43N3nfp4s3hxGrZTPRDjtK+Xd9UYSScXUSKhr6yvQyoX+snK0SMnGYjW9Xi6O7HbNoysnCBznawO60mykByVTWKbsU3GskDckaTDhRv1rf/49T+WhoPILziUZAHmO+rN5ZOvh2r+32/l0xFu0R4/YuOIU7tc4JQYfd7Vj2kH84rLevw9g7RsgD4uAz6GC3khBVwiDQ/MtQ1etk81Fz4t9VlE80osdhO0GACMdowwcNhAaFJRvP7gO161/Dq/ow5kB0IVEp0jRO3B57TT6XRctlWT+CpvR/nuVnHu/ouf+fLoc8/4ey9kTw57Z6eDnaZf257nTxGZYF2LkCrXRRZ6YWqSZnFFHn3bjd+Vin6HKYy6g1O8kuSeK8Pnvg03zPI/WFjYRM6+saLUb4v68enFzwn/HACBHEbvVglRu9Cr3QnRu6kJUUYbBBDAkL8PhCsStzVev1M0dwu1RVcDNxfnXwFl9IeRsM8cpPeOk3/vptU32fViQR5Kk75F2NkCm9V+cSSEJeuXVHrQJ4ORc35gqrSZqPEw7I5tObWi8YUTy8gEcp2BP2ubRKdFWEqm/YX20Juvf+f1vdfIhqtvaJ/QKM3cHgOXT4gtruC2KKCwPDH6iMt8md0gFlCbYvuBaudREaZ8zSaB6UZ42TBckoedfrs4+H1e36LlQkCuqbhCAXEzrkxdk9unxM7nhDiPYA35ZbTYVApqAn0X03PsG69WxOF3n3z/kfS2MFksp84h2JBp1arJVFSVqSvTtAjj+4le9E18icqpVBUplF7x94YR6uVNX7UJrLbJla+tHfoD4vhbr//O1y3fkcJnQodgqfiwEtkGn/6QhbmIw38Fo4GxRDLlcKOFqZo1C9GbnqrW7ivUEIPF5RZ5G0TFP2IDpPxKb+U9Yvl78tXvCvYQf1AB10OaOTI5Lujp9cn6F0V1iqCSpWhhgC6voFPnJ9+gQlpUrIpr3/P6HzzYe3We9Z2pZGE9coSkhgAPBlqH2lXjUE0RUqVeGr7nHhA7KP6Nq8bIZmCXdA3vyKcBAO3o4ok3XP/W1y3ekYslN40T34wAoSr4EkZobny4uDRq4bzE5thn/ofARWCRAmyd3ll7qsjiK7xgT1oH/fQKOZVzhVh5vVA3iYNvbussgc3oRkOrquTNAkhsP1tvPBuNAL5k7TNi3sFL4FmZofFxS/+AuP59t/3A6uphqe3G5KzVu7qAO9qs3YXEDIusylSVhjr1Dd8yCi1Xn4cxcl2pd1RtDEI4Keu2ztPsLa99+xsP3Y4UGeKrEYWDMb5SAHEN/LWZ+Ylz6rRgc0YCaBhuGpGg4JqGBCymGJ/y86NHfmrBPchhys12YeHxVxA+jcwFedPydR/KTvxUVRU5sp5zv9psf0Tap3QOuHsGEdZ4COhbs1AXr1t97XuF/xmiByTVRMqdBl5JHucKcJgeTgjwr2BAeJ1oJ77Z2R3/9eZvTKtRaUuX1DZtG1U2skQMR5PB7iFjUMYZ7RLNB2X6yM6tbekX84M3Hnvtq4/ftiwOGJHwB93YJKxSHHDGVrSsEeT5KK7hcXPHrSCY1eBLZCV8pmQB7ebus3+YtV80ZjdaRAQ2fLLE30MPWVu8Y/Dqd4pwVKmBRDSfnWu3v2TEBSZglU+hmD4YWFjJ5x8Zwvb+j1DaGbIix0qM7PEnrgwn86KBVR6agS+5XCDNi/zk6pHBwko9tM0oaJtmoZ+FQeJyXeVZO8jbhdwu5e0SwjxTF6Yqenbl5pO3vOU133Hd4g2FWFB8mUbzkQsbiI6S7eCgO55bnWPuB+7nlVb2aALsvF/AGGuJ3OnTWw/80op/BCkqxhd+TGXMZ22Ta7HUXvNz2bVvFOFYkNfSmKn7xZnfrS7+de6HvtxW3R9OwmjoldZaA+Svu0csLAk1CL5ANAaDYdtxnnAKgsLDONY6/moFXBl9gxWbyJBqUU3EcG3n3NmN05uj9RoCCWVAPADDKpOFfHBg+eDBlSMLg8FStrgklqUo+OKA4LwqJAD5DDM71M1pya6XfHsNNK+0xnxz/p9poPnzuzQhrnDodvZld+6XxMYf66aC7jTw8Ej90OVpXrYH5eCwvul/Jv0DQRScWwYMYUu0Dwwf+72+vVe3X+NLIrQpCc2JLPH19tJHVk7eLsQNQh6qohLyzWguHujFJVmUHy8b74OUELQ4aRxPwixYPoVuordB5Iy7+C4TqkasjSvjoEAOJ4wp+Ng80wAXcroPADHFVTiOuhLtwJzJAO5GnB5n7uYjNKV63ZNerZwYPThZu1MjPoV9lAMmAagSyWyVGHMsP/Sm0L/BhUU+U4gWufGrwrx7cOB7xpa/gkJKlsrEtNmUc1gWUvdxMX2Y01fAQOMEH75Gx4gmJqycfjY1CqBED4MIE5a3FLpJMt/rieVMLGVh8aA4sSKOLohDuVgpxDIcfS8sFWExEX3mVKGAnBpoVldDRzO9x39YWXxxReRdYihA85NMGFgBQfNMu/a5Qm7ApViZtirXJv7ZcKiVUcmBY+L4a4VY07KUttJhMxWbfHSCmPbIdf3FJRMfwjeiaVILb81eca3E/3Fbdwmxxd/74zuvCGMNdJbf8Vfso3dG6+j+fgEn3cQKLoI6B51IWAwYOGoOTSeqJlwkHXqw48bnRLOrBxKPnW6M44k94vzZ3BQ4+XUFsPJHX1v0QW6I+m4/uj8xFu03IrX8G9fQzbgaBRmNGYnpWuo+LdwnRfMJ0XxWNHdqe6eoPy30kyajJ0InpJwK1XASDH1TZlFOq+1HxM6XhdiJj3b4tjktAJMCuLLAB1JRzwkACuw4/64NOuOCdBr6TUXm32WKDzK5jzu6Z1kofgrjvNdrfAlCrewOj/cxxRVdmZss1Wt+28oXHTPcmYsHx0/9h3T7z1Oxa1XSmsL5JuePXQVU3FB4jkl3sNVpNdpczgspBtultpk2aa2mm4cRyfsN2tMejUNbi4IhKqy1r/yRsPSB4uafFOJN01al3VNqW0muqWDWBvw4fx5h5WOa+BkTv0688M3MtYO6q+IOz2hk1fhk1yNskGPOSSLwxwalQwOycYWoCrtVm9W5YQ1MAdD/XW0/dfGBn1/1jyP44Y9ZQJhCbVqYedMmSCutdIWyfYMsMIz5DrwrAFGb8ifIEsR2wyINiHrHIuXDH+8zii6GY1qIvt/R1y/f9vNC/FDbCNgKSlPXaw0PtzeFxi3MKpS563/cAs+XwDGDFzd3n9EJzU7y8v0vumtwMzGdRbLzY+u2Kr06703QsES7Rssz1ekv9PSuBIxWGe+NrQytSir9wNgsaXzqJ6nYUa4li7ANqkr0Zq5KuHMEsCFDypD4sBhsAR+iVUDYxJ9p0atQiER/dfzcX4lmB8YfCFhXoWn0MYpAHVgYksRD3INeIAWD4GEbp3hQ/IsLz8eCa8kOC2KQWAmnqqLI8ssop5Fi7XOTg8btV3HZRAvoN8X47mr9s6na4ow0kG0rCjFdKnS0UhZiy5ASQZzzgaILyxt/wyHqcOxE1tTppNYtlz8iWkKuqEJFUUMgzHBrMrxbTD7P5RRtbWBZcBXwoZQxMiYnRLYzitjiTAT0UsI3vP5FNLOwcRuh7DwXMY2VxBpeUs9cdIUzWByLU/70nyykT2oz5lRKL07gzRgr6amSaFpZlDeNTyYhmSgNh0Z3gtO0jMapbKKSodZTTinwxxa5dkHoEcw2Ar9Mn6lHnxDp/YKvBWlgGTuL2KALOUldaDkr3QVd4fDswRpltxNPlI5w/SVogsCait3oSqSvG5LLIriO2VruuciMxfDh3Z37dAZJ7KbxJZN4MB0YZFJkI29I3iuH4LSynMiKTCKrhXuC03cs6BxniFCw3xShzSHLdU6pbKfCN6Px8LOiuhvjZPnLYqCJ4mQ5LHsCJel6HbFhXBC3CChQYiP7Ze+rrsSbeO8lhU+fgGl32EEeqQsq5qWkSOJD0PlInR2tPykLA6XnpKk4MGkW6+xgnR2o1QErVtuwUId+LZem4vDUH2z0AsAEv+Q2LAl/jXCHvF9xdjmEguLDriy5cND6I9Zd47Ll3d3D7fRELlcTMRqduk/4Sfw74ehllDYMUexsxzrgiOVSite+XIlWtrseUL5QgOwM0H1Mu4yLujkvGWTD8wdYGx8/8/xfZMkjSXM28/3gF0pUAK/jrUGc5UwIrtUoMv69HMTjE+Ntzj860MCVBbfaiMXG+NZsSTM2PiQWobuBlWyltvxFSN9syEWVLvSHlZpulK8+cMNPFte8n39ShC9tITyN84rAWcHudm8L7BHajJ/4vvsEfPggZPv0EgV9Se+7S7lF3fgONcWM+bKpbZFl6flhHf2JCM8JgTLlj8G7nHNAfEYN/U/4WhX45i+EOQZ/0AY6tIK/KSAvcrbbHhfuOjqi/Dmht4UdCLcguFZlyNVwdoWmRAz4y3nJRWF3RH6b0N8t+u9oYof5aw7sL8NbITPo+Atv+uGCGSh7qO5T18UOUFzzkh53h929oFk9FkkEYI0J3jyE2sL8f3fg23Q59GKN+Db9LdG3Yb0q9G1Yrwp9G9arQt+G9arQt2G9CiTE/wU5EKZz0gCJugAAAABJRU5ErkJggg=="

    override fun addGame(game: Game){
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.gameName, game.name)
        contentValues.put(DatabaseHandler.gameDescription, game.description)
        contentValues.put(DatabaseHandler.gameIconText, defaultGameIcon)

        val success = db.insert(DatabaseHandler.tableGames,null,contentValues)
        db.close()
    }


    override fun getGames(): ArrayList<Game> {
        val gameList: ArrayList<Game> = ArrayList()

        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.gameId,
            DatabaseHandler.gameName,
            DatabaseHandler.gameDescription,
            DatabaseHandler.gameIconText
        )

        try{
            cursor = db.query(
                DatabaseHandler.tableGames,
                columns,
                null,
                null,
                null,
                null,
                null
            )

        } catch (e: SQLiteException) {
            db.close()
            return ArrayList()
        }

        var game = Game()
        if(cursor.moveToFirst()) {
            do {
                game = Game()
                game.id = cursor.getInt(0)
                game.name = cursor.getString(1)
                game.description = cursor.getString(2)

                getIcon(cursor,game, 3)

                gameList.add(game)

            }while(cursor.moveToNext())
        }
        cursor?.close()
        db.close()
        return gameList
    }

    override fun getGameByName(gameName: String): Game {
        var gameResult = Game()

        var databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase
        var cursor: Cursor? = null

        val columns = arrayOf(
            DatabaseHandler.gameId,
            DatabaseHandler.gameName,
            DatabaseHandler.gameDescription,
            DatabaseHandler.gameIconText
        )

        try{

            cursor = db.query(
                DatabaseHandler.tableGames,
                columns,
                "${DatabaseHandler.gameName} = ?",
                arrayOf(gameName),
                null,
                null,
                DatabaseHandler.gameId
            )

        } catch (e: SQLiteException) {
            db.close()
            return gameResult
        }

        var game = Game()
        if(cursor.moveToFirst()) {
            do {
                game.id = cursor.getInt(0)
                game.name = cursor.getString(1)
                game.description = cursor.getString(2)

                getIcon(cursor!!,game,3)

                gameResult = game

            }while(cursor.moveToNext())
        }
        cursor?.close()
        db.close()
        return gameResult
    }

    override fun getGameId(game: Game): Int {
        var gameId: Int = -1

        val columns = arrayOf(DatabaseHandler.gameName)

        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.query(
                DatabaseHandler.tableGames,
                columns,
                "${DatabaseHandler.gameName} like '${game.name}'",
                null,
                null,
                null,
                DatabaseHandler.gameId
            )
        } catch (e: SQLiteException) {
            db.close()
            return gameId
        }

        var game = Game()
        if (cursor.moveToFirst()) {
            do {
                game = Game()
                game.id= cursor.getInt(0)

                gameId = game.id
            } while (cursor.moveToNext())
        }
        cursor?.close()
        db.close()
        return gameId
    }


    override fun updateGame(gameId: Int, game: Game) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DatabaseHandler.gameName, game.name)
//        contentValues.put(DatabaseHandler.gameIcon, game.icon)

        val values = arrayOf("$gameId")
        val success = db.update(
            DatabaseHandler.tableGames,
            contentValues,
            "${DatabaseHandler.gameId} = ?",
            values)
        db.close()
    }

    override fun deleteGame(gameId: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
        val db = databaseHandler.writableDatabase

        val values = arrayOf("$gameId")
        val success = db.delete(
            DatabaseHandler.tableGames,
            "${DatabaseHandler.gameId} = ?",
            values)
        db.close()
    }

    override fun getIcon(cursor: Cursor, game: Game, columnIndex: Int) {
        val iconBitmap: Bitmap

        try {
            val iconText:String = cursor.getString(columnIndex)
            val iconByte:ByteArray = android.util.Base64.decode(iconText, android.util.Base64.DEFAULT)

            iconBitmap = iconByte.let { BitmapFactory.decodeByteArray(it, 0, it.size) }!!
            game.icon = iconBitmap
        }catch (e:Exception){
            Log.e("Error", "icon text is bad/empty or icon bytearray is null",e)
        }
    }
}