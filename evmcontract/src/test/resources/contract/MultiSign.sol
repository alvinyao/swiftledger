pragma solidity ^0.4.12;

interface VerifyMultiSign {
    //@param  signedStrs：signature info
    function verifyMultiSign(address _from, address _to, uint amount, bytes signedStrs) external returns (bool verifyResult);
}

contract MultiSign is VerifyMultiSign {
    address[] allAddrs;             //Create all addresses of a multisigned contract
    uint16 verifyNum;               //Number of verified signatures (must be greater than zero)
    address[] mustAddrs;            //An array of addresses that must be checked
    uint8 constant SIGN_LEN = 65;   //signature length
    uint count = 0;

    uint allAddrsLen;
    uint mustAddrsLen;

    event Log(uint _mustHave, uint _mustAddrsLen, uint _success, uint _verifyNum);

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
    mapping(address => bool) checkRepeatSignMap;

    function verifyMultiSign(address _from, address _to, uint amount, bytes signedStrs) external returns (bool verifyResult){
        uint signedNum = signedStrs.length / SIGN_LEN;
        require(signedNum >= verifyNum, "Too few signatures");
        require(signedNum <= allAddrsLen, "Too many signatures");
        bytes32 sourceHash = getSourceHash(_from, _to, amount);
        address addr;
        uint success = 0;
        uint mustHave = 0;
        for (uint i = 0; i < signedNum; i++) {
            addr = recovery(slice(signedStrs, i * SIGN_LEN, SIGN_LEN), sourceHash);
            if (!allAddrsMap[addr]) {
                continue;
            }
            if (success == 0) {
                // The first address to be approved
                checkRepeatSignMap[addr] = true;
            } else {
                //check duplicate signatures
                if (checkRepeatSignMap[addr]) {
                    restoreCheckMap();
                    //error log
                    require(checkRepeatSignMap[addr], "duplicate signatures");
                    return false;
                }
                checkRepeatSignMap[addr] = true;
            }
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
        emit Log(mustHave, mustAddrsLen, success, verifyNum);
        if (success == 0) {
            return false;
        }
        restoreCheckMap();
        require(mustHave == mustAddrsLen, "The array of addresses that must be checked does not pass verification");
        require(success == verifyNum, "Multiple signature verification failed.");
        count++;
        return true;
    }

    function initMap() private {
        for (uint i = 0; i < allAddrsLen; i++) {
            require(allAddrs[i] != 0x00, "all address array has 0x00 address");
            require(!allAddrsMap[allAddrs[i]], "duplicate address");
            allAddrsMap[allAddrs[i]] = true;
        }
        for (uint j = 0; j < mustAddrsLen; j++) {
            require(allAddrsMap[mustAddrs[j]], "The required address does not exist in all addresses arrays");
            mustAddrsMap[mustAddrs[j]] = true;
        }
    }

    function getAllParam() public view returns (address[] all, address[] must, uint16 verifyNumber){
        return (allAddrs, mustAddrs, verifyNum);
    }

    function getRandomNumber() public view returns (uint randomNumber){
        return count;
    }

    function getSourceHash(address _from, address _to, uint amount) public view returns (bytes32 sourceHash){
        require(_from != 0x0, "from address is 0x0");
        require(_to != 0x0, "from address is 0x0");
        require(amount > 0, "The amount is less than or equal to 0");
        return sha256(abi.encodePacked(allAddrs, mustAddrs, verifyNum, _from, _to, amount, count));
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

    function restoreCheckMap() private {
        for (uint i = 0; i < allAddrsLen; i++) {
            checkRepeatSignMap[allAddrs[i]] = false;
        }
    }
}
