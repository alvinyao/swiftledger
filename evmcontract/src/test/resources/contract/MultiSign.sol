pragma solidity ^0.4.12;

interface VerifyMultiSign {
    //@param  signedStrs：signature info
    // @param  sourceHash：source data hash
    function verifyMultiSign(bytes signedStrs, bytes32 sourceHash) external view returns (bool verifyResult);
}

contract MultiSign is VerifyMultiSign {
    address[] allAddrs;             //创建多重签名合约的所有地址
    uint16 verifyNum;               //验签数量（必须大于0）
    address[] mustAddrs;            //必须验签的地址数组
    uint8 constant SIGN_LEN = 65;   //签名长度

    uint allAddrsLen;
    uint mustAddrsLen;

    event Log(uint len1, uint len2, uint len3);

    constructor (
        address[] _allAddrs,
        uint16 _verifyNum,
        address[] _mustAddrs
    ) public {
        require(_verifyNum >= 1, "The total number of addresses that must be checked is less than 1");
        require(_verifyNum <= _allAddrs.length, "The number of signatures is greater than the total number of addresses");
        require(_mustAddrs.length <= _verifyNum, "The total number of addresses that must be checked is greater than the number of addresses that need to be checked");
        allAddrs = _allAddrs;
        verifyNum = _verifyNum;
        mustAddrs = _mustAddrs;
        allAddrsLen = allAddrs.length;
        mustAddrsLen = mustAddrs.length;
        initMap();
    }
    mapping(address => bool) allAddrsMap;
    mapping(address => bool) mustAddrsMap;

    function verifyMultiSign(bytes signedStrs, bytes32 sourceHash) external view returns (bool verifyResult){
        uint signedNum = signedStrs.length / SIGN_LEN;
        require(signedNum >= verifyNum, "The number of signatures is less than the number of checkups");
        address addr;
        uint success = 0;
        uint mustHave = 0;
        for (uint i = 0; i < allAddrsLen; i++) {
            addr = recovery(slice(signedStrs, i * SIGN_LEN, SIGN_LEN), sourceHash);
            if (allAddrsMap[addr]) {
                if (mustHave < mustAddrsLen) {
                    if (mustAddrsMap[addr]) {
                        mustHave++;
                    }
                }
                success++;
                if ((success == verifyNum) && (mustHave == mustAddrsLen)) {
                    break;
                }
            }
        }
        emit Log(mustHave, mustAddrsLen, success);
        require(mustHave == mustAddrsLen, "The array of addresses that must be checked does not pass verification");
        require(success == verifyNum, "Multiple signature verification failed.");
        return true;
    }

    function initMap() private {
        for (uint i = 0; i < allAddrsLen; i++) {
            require(allAddrs[i] != 0x00, "all address array has 0x00 address");
            require(!allAddrsMap[allAddrs[i]], "repeat address");
            allAddrsMap[allAddrs[i]] = true;
        }
        for (uint j = 0; j < mustAddrsLen; j++) {
            require(allAddrsMap[mustAddrs[j]], "The required address does not exist in all addresses arrays");
            mustAddrsMap[mustAddrs[j]] = true;
        }
        emit Log(allAddrsLen, mustAddrsLen, verifyNum);
    }

    function getAllParam() public view returns (address[] all, address[] must, uint16 verifyNumber){
        return (allAddrs, mustAddrs, verifyNum);
    }

    function recovery(bytes sig, bytes32 hash) private pure returns (address) {
        bytes32 r;
        bytes32 s;
        uint8 v;
        //Check the signature length
        require(sig.length == 65, "signature length not match");

        // Divide the signature in r, s and v variables
        assembly {
            r := mload(add(sig, 32))
            s := mload(add(sig, 64))
            v := byte(0, mload(add(sig, 96)))
        }
        // Version of signature should be 27 or 28
        if (v < 27) {
            v += 27;
        }
        //check version
        if (v != 27 && v != 28) {
            return address(0);
        }
        return ecrecover(hash, v, r, s);
    }

    function slice(bytes memory data, uint start, uint len) private pure returns (bytes){
        bytes memory b = new bytes(len);
        for (uint i = 0; i < len; i++) {
            b[i] = data[i + start];
        }
        return b;
    }
}
